package no.nav.tiltakspenger.dokument.brev

import no.nav.tiltakspenger.domene.brev.BrevDTO

interface BrevService {
    suspend fun arkiverBrevIJoark(brev: BrevDTO, callId: String): String
}
