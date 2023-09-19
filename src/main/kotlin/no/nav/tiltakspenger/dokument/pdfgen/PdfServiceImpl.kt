package no.nav.tiltakspenger.soknad.api.pdf

import no.nav.tiltakspenger.dokument.søknad.SøknadDTO
import no.nav.tiltakspenger.dokument.søknad.Vedlegg
import no.nav.tiltakspenger.domene.brev.BrevDTO

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
