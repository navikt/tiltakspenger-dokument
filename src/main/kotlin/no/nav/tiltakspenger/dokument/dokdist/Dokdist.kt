package no.nav.tiltakspenger.dokument.dokdist

data class DokdistDTO(
    val journalpost: String,
    val batchId: String,
    val bestillendeFagsystem: String,
    val dokumentProdApp: String,
    val distribusjonstype: DistribusjonsType,
    val distrubusjonstidspunkt: Distribusjonstidspunkt,
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
