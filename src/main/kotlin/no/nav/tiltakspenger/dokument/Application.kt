package no.nav.tiltakspenger.dokument

import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.ApplicationStopped
import io.ktor.server.application.install
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.routing.routing
import mu.KotlinLogging
import no.nav.tiltakspenger.dokument.health.healthRoutes

fun main(args: Array<String>) {
    System.setProperty("logback.configurationFile", "egenLogback.xml")

    val log = KotlinLogging.logger {}
    val securelog = KotlinLogging.logger("tjenestekall")

    Thread.setDefaultUncaughtExceptionHandler { _, e ->
        log.error { "Uncaught exception logget i securelog" }
        securelog.error(e) { e.message }
    }

    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    System.setProperty("logback.configurationFile", "egenLogback.xml")

    val log = KotlinLogging.logger {}
    val securelog = KotlinLogging.logger("tjenestekall")

    Thread.setDefaultUncaughtExceptionHandler { _, e ->
        log.error { "Uncaught exception logget i securelog" }
        securelog.error(e) { e.message }
    }

    install(CallLogging)

    routing {
        healthRoutes()
    }

    environment.monitor.subscribe(ApplicationStarted) {
        log.info { "Starter server" }
    }
    environment.monitor.subscribe(ApplicationStopped) {
        log.info { "Stopper server" }
    }
}
