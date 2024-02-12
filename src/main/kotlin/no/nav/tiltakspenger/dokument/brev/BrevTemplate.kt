package no.nav.tiltakspenger.dokument.brev

sealed class BrevTemplate(
    private val pdfTemplate: PdfTemplate,
    private val brevTittel: String,
) {
    fun template() = pdfTemplate.name()
    fun tittel() = brevTittel

    data object InnvilgetVedtak : BrevTemplate(
        pdfTemplate = PdfTemplate.InnvilgetVedtak,
        brevTittel = "Vedtaksbrev for søknad om tiltakspenger", // TODO Spør en voksen om riktig tittel på brevet
    )
}
