package no.nav.tiltakspenger.dokument.brev

import no.nav.tiltakspenger.dokument.joark.JoarkResponse

data class BrevResponse(
    val joarkResponse: JoarkResponse,
    val bestillingId: String,
)
