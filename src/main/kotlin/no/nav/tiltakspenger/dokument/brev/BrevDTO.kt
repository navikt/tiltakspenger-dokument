package no.nav.tiltakspenger.domene.brev

import java.time.LocalDateTime
class BrevInnhold(
    val personalia: Personalia,
    val fraDato: String,
    val tilDato: String,
    val innsendingTidspunkt: LocalDateTime,
)

data class Personalia(
    val dato: String,
    val fødselsnummer: String,
    val fornavn: String,
    val etternavn: String,
    val saksnummer: String,
)
