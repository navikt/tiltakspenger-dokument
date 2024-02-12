package no.nav.tiltakspenger.dokument.dokdist

interface DokdistService {
    suspend fun distribuerJournalpost(journalpostId: String, callId: String): String
}
