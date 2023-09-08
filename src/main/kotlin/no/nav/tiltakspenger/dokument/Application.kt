package no.nav.tiltakspenger.dokument

import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.ApplicationStopped
import io.ktor.server.application.install
import io.ktor.server.plugins.callid.callIdMdc
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.request.httpMethod
import io.ktor.server.routing.routing
import mu.KotlinLogging
import no.nav.tiltakspenger.dokument.brev.BrevServiceImpl
import no.nav.tiltakspenger.dokument.brev.brevRoutes
import no.nav.tiltakspenger.dokument.health.healthRoutes
import no.nav.tiltakspenger.dokument.pdfgen.PdfClient
import no.nav.tiltakspenger.dokument.søknad.SøknadServiceImpl
import no.nav.tiltakspenger.dokument.søknad.søknadRoutes
import no.nav.tiltakspenger.soknad.api.joark.JoarkService
import no.nav.tiltakspenger.soknad.api.pdf.PdfServiceImpl

fun main(args: Array<String>) {
    System.setProperty("logback.configurationFile", "egenLogback.xml")

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
    val joarkService = JoarkService(environment.config)
    val pdfService = PdfServiceImpl(
        PdfClient(
            config = environment.config,
            client = httpClientCIO(timeout = 10L),
        ),
    )
    val søknadService = SøknadServiceImpl(pdfService, joarkService)
    val brevService = BrevServiceImpl(pdfService, joarkService)

    val log = KotlinLogging.logger {}
    installCallLogging()

    routing {
        healthRoutes()
        søknadRoutes(søknadService)
        brevRoutes(brevService)
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
        format { call ->
            val status = call.response.status()
            val httpMethod = call.request.httpMethod.value
            val req = call.request
            val userAgent = call.request.headers["User-Agent"]
            "Status: $status, HTTP method: $httpMethod, User agent: $userAgent req: $req"
        }
    }
}
