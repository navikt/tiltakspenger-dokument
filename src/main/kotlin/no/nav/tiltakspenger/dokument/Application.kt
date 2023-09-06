package no.nav.tiltakspenger.dokument

import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.ApplicationStopped
import io.ktor.server.application.install
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.routing.routing
import mu.KotlinLogging
import no.nav.tiltakspenger.dokument.health.healthRoutes
import no.nav.tiltakspenger.dokument.pdfgen.PdfClient
import no.nav.tiltakspenger.dokument.søknad.SøknadServiceImpl
import no.nav.tiltakspenger.dokument.søknad.søknadRoutes
import no.nav.tiltakspenger.soknad.api.joark.JoarkClient
import no.nav.tiltakspenger.soknad.api.joark.JoarkServiceImpl
import no.nav.tiltakspenger.soknad.api.joark.TokenServiceImpl
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
    val log = KotlinLogging.logger {}
    val joarkService = JoarkServiceImpl(
        joark = JoarkClient(
            config = environment.config,
            tokenService = TokenServiceImpl(),
        ),
    )
    val pdfService = PdfServiceImpl(
        PdfClient(
            config = environment.config,
            client = httpClientCIO(timeout = 10L),
        ),
    )
    val søknadService = SøknadServiceImpl(pdfService, joarkService)
    install(CallLogging)

    routing {
        healthRoutes()
        søknadRoutes(søknadService)
    }

    environment.monitor.subscribe(ApplicationStarted) {
        log.info { "Starter server" }
    }
    environment.monitor.subscribe(ApplicationStopped) {
        log.info { "Stopper server" }
    }
}
