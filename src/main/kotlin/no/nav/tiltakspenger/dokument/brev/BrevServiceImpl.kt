package no.nav.tiltakspenger.dokument.brev

import no.nav.tiltakspenger.dokument.joark.JoarkService
import no.nav.tiltakspenger.domene.brev.BrevDTO
import no.nav.tiltakspenger.soknad.api.pdf.PdfService

class BrevServiceImpl(
    private val pdfService: PdfService,
    private val joarkService: JoarkService,
) : BrevService {
    override suspend fun arkiverBrevIJoark(brev: BrevDTO, callId: String): String {
        val brevPDF = pdfService.lagBrevPdf(brev)
        return joarkService.sendBrevPdfTilJoark(pdf = brevPDF, brevDTO = brev, callId = callId)
    }
}
