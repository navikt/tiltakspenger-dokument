package no.nav.tiltakspenger.dokument.brev

import io.ktor.http.ContentType
import io.ktor.server.application.call
import io.ktor.server.plugins.callid.callId
import io.ktor.server.request.receive
import io.ktor.server.response.respondBytes
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import mu.KotlinLogging
import no.nav.tiltakspenger.domene.brev.BrevDTO

val log = KotlinLogging.logger { }
fun Route.brevRoutes(brevService: BrevService) {
    post("/brev") {
        val brevDTO = call.receive<BrevDTO>()

        val journalpostId = brevService.arkiverBrevIJoark(brevDTO, call.callId!!)

       /* val joarkResponse = JoarkResponse(
            journalpostId = journalpostId,
            innsendingTidspunkt = brevDTO.innsendingTidspunkt,
        )*/
        call.respondBytes(
            journalpostId,
            contentType = ContentType.Application.Pdf,
        )
        // call.respond(status = HttpStatusCode.OK, message = joarkResponse)
    }
}
