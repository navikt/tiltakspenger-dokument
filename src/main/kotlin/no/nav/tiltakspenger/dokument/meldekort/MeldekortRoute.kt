package no.nav.tiltakspenger.dokument.meldekort

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.plugins.callid.callId
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import mu.KotlinLogging
import no.nav.tiltakspenger.dokument.joark.JoarkResponse

val log = KotlinLogging.logger { }
fun Route.meldekortRoutes(meldekortService: MeldekortService) {
    post("/meldekort/arkivmeldekort") {
        log.info { "Mottatt meldekort" }
        val meldekortDTO = call.receive<DokumentMeldekortDTO>()
        log.info { "Vi skjønte meldekortet" }

        val journalpostId = meldekortService.arkivMeldekortIJoark(meldekortDTO, call.callId!!)

        val joarkResponse = JoarkResponse(
            journalpostId = journalpostId,
            innsendingTidspunkt = meldekortDTO.innsendingTidspunkt,
        )

        log.info { "Det gikk : $joarkResponse" }
        call.respond(status = HttpStatusCode.OK, message = joarkResponse)
    }
}
