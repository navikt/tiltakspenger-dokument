package no.nav.tiltakspenger.dokument.dokdist

data class DokdistDTO(
    val journalpostId: String,
    val bestillendeFagsystem: String,
    val dokumentProdApp: String,
    val distribusjonstype: DistribusjonsType,
    val distribusjonstidspunkt: Distribusjonstidspunkt,
)

data class DokdistResponse(
    val bestillingId: String,
)

enum class DistribusjonsType(val value: String) {
    VEDTAK("VEDTAK"),
    VIKTIG("VIKTIG"),
    ANNET("ANNET"),
}

enum class Distribusjonstidspunkt(val value: String) {
    UMIDDELBART("UMIDDELBART"),
    KJERNETID("KJERNETID"),
}
