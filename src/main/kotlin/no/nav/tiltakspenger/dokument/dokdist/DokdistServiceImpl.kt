package no.nav.tiltakspenger.dokument.dokdist

import io.ktor.server.config.ApplicationConfig

class DokdistServiceImpl(
    applicationConfig: ApplicationConfig,
    private val dokdistClient: DokdistClient = DokdistClient(applicationConfig),
) : DokdistService {
    override suspend fun distribuerJournalpost(journalpostId: String, callId: String): String {
        val dokdistDTO = DokdistDTO(
            journalpostId = journalpostId,
            bestillendeFagsystem = "IND",
            dokumentProdApp = "tiltakspenger-dokument",
            distribusjonstype = DistribusjonsType.VEDTAK,
            distrubusjonstidspunkt = Distribusjonstidspunkt.KJERNETID,
        )
        return dokdistClient.distribuerJournalpost(dokdistDTO, callId)
    }
}
