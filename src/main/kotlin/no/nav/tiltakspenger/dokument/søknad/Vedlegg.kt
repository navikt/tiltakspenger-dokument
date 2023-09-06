package no.nav.tiltakspenger.dokument.søknad

data class Vedlegg(
    val filnavn: String,
    val contentType: String,
    val dokument: ByteArray,
    val brevkode: String = "S1",
)
