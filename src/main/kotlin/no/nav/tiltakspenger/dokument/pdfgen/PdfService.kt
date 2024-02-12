package no.nav.tiltakspenger.dokument.pdfgen

import no.nav.tiltakspenger.dokument.brev.BrevDTO
import no.nav.tiltakspenger.dokument.søknad.SøknadDTO
import no.nav.tiltakspenger.dokument.søknad.Vedlegg

interface PdfService {
    suspend fun lagSøknadPdf(søknadDTO: SøknadDTO): ByteArray
    suspend fun lagBrevPdf(brevDTO: BrevDTO): ByteArray
    suspend fun konverterVedlegg(vedlegg: List<Vedlegg>): List<Vedlegg>
}
