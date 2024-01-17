package no.nav.tiltakspenger.dokument.vedtak

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import mu.KotlinLogging

val log = KotlinLogging.logger { }

fun Route.vedtakRoutes() {
    post("/dokument/vedtak") {
        val vedtakDTO = call.receive<String>()

        call.respond(status = HttpStatusCode.OK, message = vedtakDTO)
    }
}
