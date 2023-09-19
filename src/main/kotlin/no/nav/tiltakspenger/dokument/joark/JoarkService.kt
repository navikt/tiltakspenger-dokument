package no.nav.tiltakspenger.dokument.joark

import no.nav.tiltakspenger.dokument.søknad.SøknadDTO
import no.nav.tiltakspenger.dokument.søknad.Vedlegg
import no.nav.tiltakspenger.domene.brev.BrevDTO

interface JoarkService {
    suspend fun sendSøknadPdfTilJoark(pdf: ByteArray, søknadDTO: SøknadDTO, vedlegg: List<Vedlegg>, callId: String): String
    suspend fun sendBrevPdfTilJoark(pdf: ByteArray, brevDTO: BrevDTO, callId: String): String
}
