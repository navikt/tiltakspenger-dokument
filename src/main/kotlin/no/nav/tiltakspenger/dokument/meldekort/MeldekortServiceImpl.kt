package no.nav.tiltakspenger.dokument.meldekort

import no.nav.tiltakspenger.dokument.joark.JoarkService

class MeldekortServiceImpl(
    private val joarkService: JoarkService,
) : MeldekortService {
    override suspend fun arkivMeldekortIJoark(meldekortDTO: MeldekortDTO, callId: String): String {
        return joarkService.sendMeldekortJsonTilJoark(meldekortDTO, callId)
    }
}
