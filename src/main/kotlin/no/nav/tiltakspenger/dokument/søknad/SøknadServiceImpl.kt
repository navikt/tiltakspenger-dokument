package no.nav.tiltakspenger.dokument.søknad

import io.ktor.http.content.MultiPartData
import no.nav.tiltakspenger.soknad.api.joark.JoarkService
import no.nav.tiltakspenger.soknad.api.pdf.PdfService

class SøknadServiceImpl(
    private val pdfService: PdfService,
    private val joarkService: JoarkService,
) : SøknadService {
    override suspend fun arkiverIJoark(søknad: SøknadDTO, vedlegg: List<Vedlegg>, callId: String): String {
        val søknadPDF = pdfService.lagSøknadPdf(søknad)
        val vedleggSomPdfer = pdfService.konverterVedlegg(vedlegg)

        return joarkService.sendPdfTilJoark(pdf = søknadPDF, dokumentJSON = søknad, vedlegg = vedleggSomPdfer, callId = callId)
    }

    override suspend fun taInnSøknadSomMultipart(søknadSomMultipart: MultiPartData): Pair<SpørsmålsbesvarelserDTO, List<Vedlegg>> {
        TODO("Not yet implemented")
    }
}
