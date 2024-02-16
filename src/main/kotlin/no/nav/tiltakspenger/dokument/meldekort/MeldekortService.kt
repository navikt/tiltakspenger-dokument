package no.nav.tiltakspenger.dokument.meldekort

interface MeldekortService {
    suspend fun arkivMeldekortIJoark(meldekortDTO: DokumentMeldekortDTO, callId: String): String
}
