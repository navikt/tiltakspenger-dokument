package no.nav.tiltakspenger.domene.brev

sealed class PdfTemplate(
    private val templateName: String,
) {
    fun name() = templateName

    // TODO skal vi ha en egen type på med/uten barnetillegg?
    data object InnvilgetVedtak : PdfTemplate("vedtakInnvilgelse")
}
