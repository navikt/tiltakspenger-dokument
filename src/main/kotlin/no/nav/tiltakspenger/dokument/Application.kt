package no.nav.tiltakspenger.dokument

import mu.KotlinLogging

fun main(args: Array<String>) {
    System.setProperty("logback.configurationFile", "egenLogback.xml")

    val log = KotlinLogging.logger {}
    val securelog = KotlinLogging.logger("tjenestekall")

    Thread.setDefaultUncaughtExceptionHandler { _, e ->
        log.error { "Uncaught exception logget i securelog" }
        securelog.error(e) { e.message }
    }

    log.info { "starting server" }

    io.ktor.server.netty.EngineMain.main(args)
}
