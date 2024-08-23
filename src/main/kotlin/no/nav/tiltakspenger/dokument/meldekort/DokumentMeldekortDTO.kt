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
    SPERRET("sperret"),
    IKKE_UTFYLT("Ikke utfylt"),
    DELTATT_UTEN_LØNN_I_TILTAKET("Deltatt uten lønn i tiltaket"),
    DELTATT_MED_LØNN_I_TILTAKET("Deltatt med lønn i tiltaket"),
    IKKE_DELTATT("Ikke deltatt i tiltaket"),
    FRAVÆR_SYK("Fravær - Syk"),
    FRAVÆR_SYKT_BARN("Fravær - Sykt barn"),
    FRAVÆR_VELFERD_GODKJENT_AV_NAV("Fravær - Velferd. Godkjent av NAV"),
    FRAVÆR_VELFERD_IKKE_GODKJENT_AV_NAV("Fravær - Velferd. Ikke godkjent av NA"),
}
