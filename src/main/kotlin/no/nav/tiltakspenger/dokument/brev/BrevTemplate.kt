package no.nav.tiltakspenger.domene.brev

sealed class BrevTemplate(
    private val pdfTemplate: PdfTemplate,
    private val brevTittel: String,
) {
    fun template() = pdfTemplate.name()
    fun tittel() = brevTittel

    // TODO Finn ut om vi skal ha eget brev for barnetillegg eller om vi ikke trenger det
    data object InnvilgetVedtak : BrevTemplate(
        pdfTemplate = PdfTemplate.InnvilgetVedtak,
        brevTittel = "Vedtaksbrev for søknad om tiltakspenger", // TODO Spør en voksen om riktig tittel på brevet
    )
}
