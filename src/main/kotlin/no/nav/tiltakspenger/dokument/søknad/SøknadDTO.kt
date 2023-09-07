package no.nav.tiltakspenger.dokument.søknad

import java.time.LocalDateTime
import java.util.UUID

data class Personopplysninger(
    val ident: String,
    val fornavn: String,
    val etternavn: String,
)

data class SøknadDTO(
    val id: UUID = UUID.randomUUID(),
    val acr: String,
    val versjon: String,
    val spørsmålsbesvarelser: SpørsmålsbesvarelserDTO,
    val vedleggsnavn: List<String>,
    val personopplysninger: Personopplysninger,
    val innsendingTidspunkt: LocalDateTime,
)
