package no.nav.tiltakspenger.dokument.pdfgen

import no.nav.tiltakspenger.dokument.brev.BrevDTO
import no.nav.tiltakspenger.dokument.søknad.SøknadDTO
import no.nav.tiltakspenger.dokument.søknad.Vedlegg

class PdfServiceImpl(
    private val pdfGenerator: PdfGenerator,
) : PdfService {
    override suspend fun lagSøknadPdf(søknadDTO: SøknadDTO) =
        pdfGenerator.genererSøknadPdf(søknadDTO)
    override suspend fun lagBrevPdf(brevDTO: BrevDTO) =
        pdfGenerator.genererBrevPdf(brevDTO)
    override suspend fun konverterVedlegg(vedlegg: List<Vedlegg>) =
        pdfGenerator.konverterVedlegg(vedlegg)
}
