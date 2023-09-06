package no.nav.tiltakspenger.soknad.api.joark

import no.nav.tiltakspenger.dokument.søknad.SøknadDTO
import no.nav.tiltakspenger.dokument.søknad.Vedlegg

interface JoarkService {
    suspend fun sendPdfTilJoark(pdf: ByteArray, søknadDTO: SøknadDTO, vedlegg: List<Vedlegg>, callId: String): String
}
