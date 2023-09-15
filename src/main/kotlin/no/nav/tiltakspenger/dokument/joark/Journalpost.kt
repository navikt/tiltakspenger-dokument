package no.nav.tiltakspenger.soknad.api.joark

import no.nav.tiltakspenger.dokument.objectMapper
import no.nav.tiltakspenger.dokument.søknad.SøknadDTO
import no.nav.tiltakspenger.dokument.søknad.Vedlegg
import java.util.Base64

enum class Tema(val value: String) {
    TILTAKSPENGER("IND"),
}

sealed class Journalpost {
    val tema: String = Tema.TILTAKSPENGER.value
    abstract val journalfoerendeEnhet: String?
    abstract val tittel: String
    abstract val journalpostType: JournalPostType
    abstract val kanal: String
    abstract val avsenderMottaker: AvsenderMottaker
    abstract val bruker: Bruker
    abstract val sak: Sak?
    abstract val dokumenter: List<JournalpostDokument>

    data class Søknadspost private constructor(
        val fnr: String,
        override val dokumenter: List<JournalpostDokument>,
    ) : Journalpost() {
        override val tittel: String = "Søknad om tiltakspenger"
        override val avsenderMottaker: AvsenderMottaker = AvsenderMottaker(id = fnr)
        override val bruker: Bruker = Bruker(id = fnr)
        override val sak: Sak? = null
        override val journalpostType: JournalPostType = JournalPostType.INNGAAENDE
        override val kanal: String = Kanal.SOKNAD.value
        override val journalfoerendeEnhet: String? = null

        companion object {
            fun from(
                fnr: String,
                søknadDTO: SøknadDTO,
                pdf: ByteArray,
                vedlegg: List<Vedlegg>,
            ) =
                Søknadspost(
                    fnr = fnr,
                    dokumenter = mutableListOf(
                        lagHoveddokument(
                            pdf = pdf,
                            søknadDTO = søknadDTO,
                            tittel = "Søknad om tiltakspenger",
                            brevkode = BrevKode.SOKNAD.value,
                        ),
                    ).apply {
                        this.addAll(lagVedleggsdokumenter(vedlegg))
                    },
                )

            private fun lagHoveddokument(pdf: ByteArray, søknadDTO: SøknadDTO, tittel: String, brevkode: String): JournalpostDokument =
                JournalpostDokument(
                    tittel = tittel,
                    brevkode = brevkode,
                    dokumentvarianter = listOf(
                        DokumentVariant.ArkivPDF(
                            fysiskDokument = Base64.getEncoder().encodeToString(pdf),
                            tittel = tittel,
                        ),
                        DokumentVariant.OriginalJson(
                            fysiskDokument = Base64.getEncoder()
                                .encodeToString(objectMapper.writeValueAsString(søknadDTO).toByteArray()),
                            tittel = tittel,
                        ),
                    ),
                )

            private fun lagVedleggsdokumenter(vedleggListe: List<Vedlegg>): List<JournalpostDokument> =
                vedleggListe.map { vedlegg ->
                    JournalpostDokument(
                        tittel = vedlegg.filnavn,
                        brevkode = vedlegg.brevkode,
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
        val fagsaksystem: String = "IND",
        val sakstype: String = "FAGSAK",
    ) : Sak()
}

data class JournalpostDokument(
    val tittel: String,
    val brevkode: String,
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

enum class BrevKode(val value: String) {
    SOKNAD("NAV 76-13.45"),
    BREV("NAV 76-13.04"), // TODO: Undersøke hvilken/hvilke brevkode(r) som brukes for vedtaksbrev
    VEDLEGG("S1"),
}

enum class FilNavn(val value: String) {
    SOKNAD("tiltakspengersoknad.json"),
    BREV("vedtaksbrev.json"),
}

enum class Kanal(val value: String) {
    SOKNAD("NAV_NO"),
    BREV(""), // TODO: Finne riktig verdi for utsendingskanal på brev
}
