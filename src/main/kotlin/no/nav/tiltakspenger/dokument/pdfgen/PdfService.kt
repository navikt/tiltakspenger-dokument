package no.nav.tiltakspenger.soknad.api.pdf

import no.nav.tiltakspenger.dokument.søknad.SøknadDTO
import no.nav.tiltakspenger.dokument.søknad.Vedlegg
import no.nav.tiltakspenger.domene.brev.BrevDTO

interface PdfService {
    suspend fun lagSøknadPdf(søknadDTO: SøknadDTO): ByteArray
    suspend fun lagBrevPdf(brevDTO: BrevDTO): ByteArray
    suspend fun konverterVedlegg(vedlegg: List<Vedlegg>): List<Vedlegg>
}
