package no.nav.tiltakspenger.dokument.brev

interface BrevService {
    suspend fun arkiverBrevIJoark(brev: BrevDTO, callId: String): String
    suspend fun distribuerJournalpost(journalpostId: String, callId: String): String
}
