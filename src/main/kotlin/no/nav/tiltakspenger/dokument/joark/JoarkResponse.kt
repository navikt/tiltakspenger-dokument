package no.nav.tiltakspenger.dokument.joark

import java.time.LocalDateTime

data class JoarkResponse(
    val journalpostId: String,
    val innsendingTidspunkt: LocalDateTime,
)
