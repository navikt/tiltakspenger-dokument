package no.nav.tiltakspenger.dokument.søknad

import java.time.LocalDateTime

data class JoarkResponse(
    val journalpostId: String,
    val innsendingTidspunkt: LocalDateTime,
)
