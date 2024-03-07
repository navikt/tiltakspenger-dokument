package no.nav.tiltakspenger.dokument.meldekort

import no.nav.tiltakspenger.dokument.joark.JoarkService
import no.nav.tiltakspenger.dokument.pdfgen.PdfTools

class MeldekortServiceImpl(
    private val joarkService: JoarkService,
) : MeldekortService {
    override suspend fun arkivMeldekortIJoark(meldekortDTO: DokumentMeldekortDTO, callId: String): String {
        val pdf = PdfTools.genererPdfFraData(meldekortDTO)
        return joarkService.sendMeldekortJsonTilJoark(pdf, meldekortDTO, callId)
    }
}
