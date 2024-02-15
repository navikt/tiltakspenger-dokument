package no.nav.tiltakspenger.dokument.meldekort

interface MeldekortService {
    suspend fun arkivMeldekortIJoark(meldekortDTO: MeldekortDTO, callId: String): String
}
