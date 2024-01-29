package no.nav.tiltakspenger.dokument

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.ApplicationStopped
import io.ktor.server.application.install
import io.ktor.server.plugins.callid.CallId
import io.ktor.server.plugins.callid.callIdMdc
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import io.ktor.server.routing.routing
import mu.KotlinLogging
import no.nav.tiltakspenger.dokument.brev.BrevServiceImpl
import no.nav.tiltakspenger.dokument.brev.brevRoutes
import no.nav.tiltakspenger.dokument.health.healthRoutes
import no.nav.tiltakspenger.dokument.pdfgen.PdfClient
import no.nav.tiltakspenger.dokument.søknad.SøknadServiceImpl
import no.nav.tiltakspenger.dokument.søknad.søknadRoutes
import no.nav.tiltakspenger.dokument.vedtak.vedtakRoutes
import no.nav.tiltakspenger.soknad.api.joark.JoarkServiceImpl
import no.nav.tiltakspenger.soknad.api.pdf.PdfServiceImpl
import java.util.UUID.randomUUID

fun main(args: Array<String>) {
    val logback = with(System.getenv("NAIS_CLUSTER_NAME")) {
        if (this == null || this == "compose") "logback.local.xml" else "logback.xml"
    }
    System.setProperty("logback.configurationFile", logback)

    val log = KotlinLogging.logger {}
    val securelog = KotlinLogging.logger("tjenestekall")

    Thread.setDefaultUncaughtExceptionHandler { _, e ->
        log.error { "Uncaught exception logget i securelog" }
        println(e.message)
        securelog.error(e) { e.message }
    }

    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val joarkService = JoarkServiceImpl(environment.config)
    val pdfService = PdfServiceImpl(
        PdfClient(
            config = environment.config,
            client = httpClientCIO(timeout = 30L),
        ),
    )
    val søknadService = SøknadServiceImpl(pdfService, joarkService)
    val brevService = BrevServiceImpl(pdfService, joarkService)
    val log = KotlinLogging.logger {}
    installCallLogging()
    installContentNegoatiation()
    install(CallId) {
        generate { randomUUID().toString() } // TODO: Sette opp tracing mellom apper
    }

    routing {
        healthRoutes()
        søknadRoutes(søknadService)
        brevRoutes(brevService)
        vedtakRoutes()
    }

    environment.monitor.subscribe(ApplicationStarted) {
        log.info { "Starter server" }
    }
    environment.monitor.subscribe(ApplicationStopped) {
        log.info { "Stopper server" }
    }
}

internal fun Application.installCallLogging() {
    install(CallLogging) {
        callIdMdc("call-id")
        disableDefaultColors()
        filter { call ->
            !call.request.path().contains("/isalive") &&
                !call.request.path().contains("/isready") &&
                !call.request.path().contains("/metrics")
        }
        format { call ->
            val status = call.response.status()
            val httpMethod = call.request.httpMethod.value
            val req = call.request
            val userAgent = call.request.headers["User-Agent"]
            "Status: $status, HTTP method: $httpMethod, User agent: $userAgent req: $req"
        }
    }
}

internal fun Application.installContentNegoatiation() {
    install(ContentNegotiation) {
        jackson {
            configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            registerModule(JavaTimeModule())
            registerModule(KotlinModule.Builder().build())
        }
    }
}
