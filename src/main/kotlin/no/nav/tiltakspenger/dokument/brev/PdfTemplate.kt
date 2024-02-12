package no.nav.tiltakspenger.dokument.brev

sealed class PdfTemplate(
    private val templateName: String,
) {
    fun name() = templateName

    data object InnvilgetVedtak : PdfTemplate("vedtakInnvilgelse")
}
