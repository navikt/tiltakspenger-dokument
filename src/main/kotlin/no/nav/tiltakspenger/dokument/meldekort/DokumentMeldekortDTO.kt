package no.nav.tiltakspenger.dokument.meldekort

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class DokumentMeldekortDTO(
    val meldekortId: UUID,
    val sakId: String,
    val meldekortPeriode: PeriodeDTO,
    val saksbehandler: String,
    val meldekortDager: List<MeldekortDagDTO>,
    val tiltak: List<TiltakDTO>,
    val innsendingTidspunkt: LocalDateTime,
    val personopplysninger: PersonopplysningerDTO,
)

data class PersonopplysningerDTO(
    val fornavn: String,
    val etternavn: String,
    val ident: String,
)

data class PeriodeDTO(
    val fom: LocalDate,
    val tom: LocalDate,
)

data class TiltakDTO(
    val id: UUID,
    val periode: PeriodeDTO,
    val typeBeskrivelse: String,
    val typeKode: String,
    val antDagerIUken: Float,
)

data class MeldekortDagDTO(
    val dato: LocalDate,
    val tiltakType: String?,
    val status: MeldekortDagStatusDTO,
)

enum class MeldekortDagStatusDTO(status: String) {
    IKKE_UTFYLT("Ikke utfylt"),
    DELTATT("Deltatt"),
    IKKE_DELTATT("Ikke deltatt"),
    FRAVÆR_SYK("Fravær syk"),
    FRAVÆR_SYKT_BARN("Fravær sykt barn"),
    FRAVÆR_VELFERD("Fravær velferd"),
    LØNN_FOR_TID_I_ARBEID("Lønn for tid i arbeid"),
}
