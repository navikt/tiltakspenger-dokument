package no.nav.tiltakspenger.dokument.dokdist

import io.ktor.server.config.ApplicationConfig

class DokdistServiceImpl(
    applicationConfig: ApplicationConfig,
    private val dokdistClient: DokdistClient = DokdistClient(applicationConfig),
) : DokdistService {
    override suspend fun distribuerJournalpost(journalpostId: String, callId: String): String {
        val dokdistDTO = DokdistDTO(
            journalpost = journalpostId,
            batchId = "",
            bestillendeFagsystem = "IND",
            dokumentProdApp = "tiltakspenger-vedtak",
            distribusjonstype = DistribusjonsType.VEDTAK,
            distrubusjonstidspunkt = Distribusjonstidspunkt.UMIDDELBART,
        )
        return dokdistClient.distribuerJournalpost(dokdistDTO, callId)
    }
}
