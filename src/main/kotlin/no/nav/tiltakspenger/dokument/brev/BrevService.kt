package no.nav.tiltakspenger.dokument.brev

import no.nav.tiltakspenger.libs.dokument.BrevDTO

interface BrevService {
    suspend fun arkiverBrevIJoark(brev: BrevDTO, callId: String): String
    suspend fun distribuerJournalpost(journalpostId: String, callId: String): String
}
