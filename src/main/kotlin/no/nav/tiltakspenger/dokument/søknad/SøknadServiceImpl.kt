package no.nav.tiltakspenger.dokument.søknad

import no.nav.tiltakspenger.dokument.joark.JoarkService
import no.nav.tiltakspenger.dokument.pdfgen.PdfService

class SøknadServiceImpl(
    private val pdfService: PdfService,
    private val joarkService: JoarkService,
) : SøknadService {
    override suspend fun arkiverIJoark(søknad: SøknadDTO, vedlegg: List<Vedlegg>, callId: String): String {
        val søknadPDF = pdfService.lagSøknadPdf(søknad)
        val vedleggSomPdfer = pdfService.konverterVedlegg(vedlegg)

        return joarkService.sendSøknadPdfTilJoark(pdf = søknadPDF, søknadDTO = søknad, vedlegg = vedleggSomPdfer, callId = callId)
    }
}
