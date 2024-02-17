package no.nav.tiltakspenger.dokument.joark

import no.nav.tiltakspenger.dokument.brev.BrevDTO
import no.nav.tiltakspenger.dokument.meldekort.DokumentMeldekortDTO
import no.nav.tiltakspenger.dokument.objectMapper
import no.nav.tiltakspenger.dokument.søknad.SøknadDTO
import no.nav.tiltakspenger.dokument.søknad.Vedlegg
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Base64

sealed class Journalpost {
    val tema: String = "IND"
    abstract val journalpostType: JournalPostType
    abstract val kanal: String
    abstract val avsenderMottaker: AvsenderMottaker
    abstract val bruker: Bruker
    abstract val dokumenter: List<JournalpostDokument>
    abstract val tittel: String
    abstract val journalfoerendeEnhet: String?
    abstract val sak: Sak?

    data class Brevpost(
        val fnr: String,
        val pdf: ByteArray,
        val brevDTO: BrevDTO,
        val saksId: String,
    ) : Journalpost() {
        override val avsenderMottaker: AvsenderMottaker = AvsenderMottaker(id = fnr)
        override val bruker: Bruker = Bruker(id = fnr)
        override val journalpostType: JournalPostType = JournalPostType.INNGAAENDE
        override val kanal: String = Kanal.BREV.value
        override val tittel: String = DokumentTittel.BREV.value
        override val journalfoerendeEnhet: String = "9999"
        override val sak: Sak = Sak.Fagsak(saksId)
        override val dokumenter = mutableListOf(
            lagBrevdokument(
                pdf = pdf,
                brevDTO = brevDTO,
            ),
        )
    }

    data class Søknadspost(
        val fnr: String,
        val pdf: ByteArray,
        val søknadDTO: SøknadDTO,
        val vedlegg: List<Vedlegg>,
    ) : Journalpost() {
        override val avsenderMottaker: AvsenderMottaker = AvsenderMottaker(id = fnr)
        override val bruker: Bruker = Bruker(id = fnr)
        override val journalpostType: JournalPostType = JournalPostType.INNGAAENDE
        override val kanal: String = Kanal.SOKNAD.value
        override val tittel: String = DokumentTittel.BREV.value
        override val journalfoerendeEnhet: String? = null
        override val sak: Sak? = null
        override val dokumenter = mutableListOf(
            lagSøknaddokument(
                pdf = pdf,
                søknadDTO = søknadDTO,
            ),
        ).apply {
            this.addAll(lagVedleggsdokumenter(vedlegg))
        }
    }

    data class Meldekortpost(
        val fnr: String,
        val meldekortDTO: DokumentMeldekortDTO,
        val saksId: String,
    ) : Journalpost() {
        override val avsenderMottaker: AvsenderMottaker = AvsenderMottaker(id = fnr)
        override val bruker: Bruker = Bruker(id = fnr)
        override val journalpostType: JournalPostType = JournalPostType.INNGAAENDE
        override val kanal: String = Kanal.MELDEKORT.value
        override val tittel: String = DokumentTittel.MELDEKORT.value
        override val journalfoerendeEnhet: String = "9999"
        override val sak: Sak = Sak.Fagsak(saksId)
        override val dokumenter = mutableListOf(
            lagMeldekortDokument(
                meldekortDTO = meldekortDTO,
            ),
        )
    }

    companion object {
        private fun lagBrevdokument(pdf: ByteArray, brevDTO: BrevDTO): JournalpostDokument =
            JournalpostDokument(
                tittel = DokumentTittel.BREV.value,
                brevkode = BrevKode.BREV,
                dokumentvarianter = listOf(
                    DokumentVariant.ArkivPDF(
                        fysiskDokument = Base64.getEncoder().encodeToString(pdf),
                        tittel = DokumentTittel.BREV.value,
                    ),
                    DokumentVariant.OriginalJson(
                        fysiskDokument = Base64.getEncoder()
                            .encodeToString(objectMapper.writeValueAsString(brevDTO).toByteArray()),
                        tittel = DokumentTittel.BREV.value,
                    ),
                ),
            )

        private fun lagMeldekortTittel(meldekortDTO: DokumentMeldekortDTO): String {
            // Meldekort for uke 5 - 6 (29.01.2024 - 11.02.2024)
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            return "Meldekort for uke ${meldekortDTO.meldekortPeriode.fom.get(WeekFields.ISO.weekOfWeekBasedYear())}" +
                " - ${meldekortDTO.meldekortPeriode.tom.get(WeekFields.ISO.weekOfWeekBasedYear())}" +
                " (${meldekortDTO.meldekortPeriode.fom.format(formatter)} - ${meldekortDTO.meldekortPeriode.tom.format(formatter)})"
        }

        private fun lagMeldekortDokument(meldekortDTO: DokumentMeldekortDTO): JournalpostDokument =
            JournalpostDokument(
                tittel = lagMeldekortTittel(meldekortDTO),
                brevkode = BrevKode.MELDEKORT,
                dokumentvarianter = listOf(
                    DokumentVariant.OriginalJson(
                        fysiskDokument = Base64.getEncoder()
                            .encodeToString(objectMapper.writeValueAsString(meldekortDTO).toByteArray()),
                        tittel = lagMeldekortTittel(meldekortDTO),
                    ),
                ),
            )

        private fun lagSøknaddokument(pdf: ByteArray, søknadDTO: SøknadDTO): JournalpostDokument =
            JournalpostDokument(
                tittel = DokumentTittel.SOKNAD.value,
                brevkode = BrevKode.SOKNAD,
                dokumentvarianter = listOf(
                    DokumentVariant.ArkivPDF(
                        fysiskDokument = Base64.getEncoder().encodeToString(pdf),
                        tittel = DokumentTittel.SOKNAD.value,
                    ),
                    DokumentVariant.OriginalJson(
                        fysiskDokument = Base64.getEncoder()
                            .encodeToString(objectMapper.writeValueAsString(søknadDTO).toByteArray()),
                        tittel = DokumentTittel.SOKNAD.value,
                    ),
                ),
            )

        private fun lagVedleggsdokumenter(vedleggListe: List<Vedlegg>): List<JournalpostDokument> =
            vedleggListe.map { vedlegg ->
                JournalpostDokument(
                    tittel = vedlegg.filnavn,
                    brevkode = BrevKode.VEDLEGG,
                    dokumentvarianter = listOf(
                        DokumentVariant.VedleggPDF(
                            fysiskDokument = Base64.getEncoder().encodeToString(vedlegg.dokument),
                            filnavn = vedlegg.filnavn,
                        ),
                    ),
                )
            }
    }
}

internal data class JournalpostRequest(
    val tittel: String,
    val journalpostType: JournalPostType,
    val tema: String,
    val kanal: String,
    val journalfoerendeEnhet: String?,
    val avsenderMottaker: AvsenderMottaker,
    val bruker: Bruker,
    val sak: Sak?,
    val dokumenter: List<JournalpostDokument>,
    val eksternReferanseId: String,
)

data class AvsenderMottaker(
    val id: String,
    val idType: String = "FNR",
)

data class Bruker(
    val id: String,
    val idType: String = "FNR",
)

sealed class Sak {
    data class Fagsak(
        val fagsakId: String,
        val fagsaksystem: String = "AO01",
        val sakstype: String = "FAGSAK",
    ) : Sak()
}

data class JournalpostDokument(
    val tittel: String,
    val brevkode: BrevKode,
    val dokumentvarianter: List<DokumentVariant>,
)

sealed class DokumentVariant {
    abstract val filtype: String
    abstract val fysiskDokument: String
    abstract val variantformat: String
    abstract val filnavn: String

    data class ArkivPDF(
        override val fysiskDokument: String,
        val tittel: String,
    ) : DokumentVariant() {
        override val filtype: String = "PDFA"
        override val variantformat: String = "ARKIV"
        override val filnavn: String = "$tittel.pdf"
    }

    data class VedleggPDF(
        override val fysiskDokument: String,
        override val filnavn: String,
    ) : DokumentVariant() {
        override val filtype: String = "PDFA"
        override val variantformat: String = "ARKIV"
    }

    data class OriginalJson(
        override val fysiskDokument: String,
        val tittel: String,
    ) : DokumentVariant() {
        override val filtype: String = "JSON"
        override val variantformat: String = "ORIGINAL"
        override val filnavn: String = "$tittel.json"
    }
}

enum class JournalPostType(val value: String) {
    INNGAAENDE("INNGAAENDE"),
    UTGAAENDE("UTGAAENDE"),
}

enum class DokumentTittel(val value: String) {
    SOKNAD("Søknad om tiltakspenger"),
    BREV("Vedtaksbrev for søknad om tiltakspenger"),
    MELDEKORT("Meldekort for tiltakspenger"),
}

enum class BrevKode(val value: String) {
    SOKNAD("NAV 76-13.45"),
    BREV("NAV 76-13.04"), // TODO: Undersøke hvilken/hvilke brevkode(r) som brukes for vedtaksbrev
    MELDEKORT("TP-MELDEKORT"),
    VEDLEGG("S1"),
}

enum class FilNavn(val value: String) {
    SOKNAD("tiltakspengersoknad.json"),
    BREV("vedtaksbrev.json"),
    MELDEKORT("meldekort.json"),
}

enum class Kanal(val value: String) {
    SOKNAD("NAV_NO"),
    BREV("NAV_NO"), // TODO: Finne riktig verdi for utsendingskanal på brev
    MELDEKORT("INNSENDT_NAV_ANSATT"), // TODO: Bekreft om denne er riktig kanal med fag https://confluence.adeo.no/display/BOA/Mottakskanal
}
