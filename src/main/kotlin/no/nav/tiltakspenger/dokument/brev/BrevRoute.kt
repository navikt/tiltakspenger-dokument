package no.nav.tiltakspenger.dokument.brev

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.plugins.callid.callId
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import mu.KotlinLogging
import no.nav.tiltakspenger.dokument.joark.JoarkResponse
import no.nav.tiltakspenger.libs.dokument.BrevDTO
import java.time.LocalDateTime

val log = KotlinLogging.logger { }
fun Route.brevRoutes(brevService: BrevService) {
    post("/brev") {
        val brevDTO = call.receive<BrevDTO>()

        val journalpostId = brevService.arkiverBrevIJoark(brevDTO, call.callId!!)
        val bestillingId = brevService.distribuerJournalpost(journalpostId, call.callId!!)

        val brevResponse = BrevResponse(
            joarkResponse = JoarkResponse(
                journalpostId = journalpostId,
                innsendingTidspunkt = LocalDateTime.now(),
            ),
            bestillingId = bestillingId,
        )
        call.respond(status = HttpStatusCode.OK, message = brevResponse)
    }
}
