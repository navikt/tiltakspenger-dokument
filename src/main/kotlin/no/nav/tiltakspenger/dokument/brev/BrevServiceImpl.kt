package no.nav.tiltakspenger.dokument.brev

import no.nav.tiltakspenger.dokument.dokdist.DokdistService
import no.nav.tiltakspenger.dokument.joark.JoarkService
import no.nav.tiltakspenger.dokument.pdfgen.PdfService
import no.nav.tiltakspenger.libs.dokument.BrevDTO

class BrevServiceImpl(
    private val pdfService: PdfService,
    private val joarkService: JoarkService,
    private val dokdistService: DokdistService,
) : BrevService {
    override suspend fun arkiverBrevIJoark(brev: BrevDTO, callId: String): String {
        val brevPDF = pdfService.lagBrevPdf(brev)
        return joarkService.sendBrevPdfTilJoark(pdf = brevPDF, brevDTO = brev, callId = callId)
    }

    override suspend fun distribuerJournalpost(journalpostId: String, callId: String): String {
        return dokdistService.distribuerJournalpost(journalpostId = journalpostId, callId = callId)
    }
}
