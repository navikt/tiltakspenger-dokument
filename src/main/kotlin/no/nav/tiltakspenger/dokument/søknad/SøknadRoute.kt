package no.nav.tiltakspenger.dokument.søknad

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.plugins.callid.callId
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import mu.KotlinLogging

val log = KotlinLogging.logger { }
fun Route.søknadRoutes(søknadService: SøknadService) {
    post("/soknad") {
        val (søknadDTO, vedlegg) = søknadService.taInnSøknadSomMultipart(call.receiveMultipart())
        val journalpostId = søknadService.arkiverIJoark(søknadDTO, vedlegg, call.callId!!)

        val joarkResponse = JoarkResponse(
            journalpostId = journalpostId,
            innsendingTidspunkt = søknadDTO.innsendingTidspunkt,
        )

        call.respond(status = HttpStatusCode.OK, message = joarkResponse)
    }
}
