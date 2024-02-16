package no.nav.tiltakspenger.dokument.joark

import no.nav.tiltakspenger.dokument.brev.BrevDTO
import no.nav.tiltakspenger.dokument.meldekort.DokumentMeldekortDTO
import no.nav.tiltakspenger.dokument.søknad.SøknadDTO
import no.nav.tiltakspenger.dokument.søknad.Vedlegg

interface JoarkService {
    suspend fun sendSøknadPdfTilJoark(pdf: ByteArray, søknadDTO: SøknadDTO, vedlegg: List<Vedlegg>, callId: String): String
    suspend fun sendBrevPdfTilJoark(pdf: ByteArray, brevDTO: BrevDTO, callId: String): String
    suspend fun sendMeldekortJsonTilJoark(meldekortDTO: DokumentMeldekortDTO, callId: String): String
}
