package no.nav.tiltakspenger.dokument.søknad

import java.time.LocalDateTime

data class SøknadResponse(
    val journalpostId: String,
    val innsendingTidspunkt: LocalDateTime,
)
