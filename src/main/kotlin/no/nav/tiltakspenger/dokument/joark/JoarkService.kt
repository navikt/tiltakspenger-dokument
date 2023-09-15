package no.nav.tiltakspenger.soknad.api.joark

import io.ktor.server.config.ApplicationConfig
import no.nav.tiltakspenger.dokument.søknad.SøknadDTO
import no.nav.tiltakspenger.dokument.søknad.Vedlegg

class JoarkService(
    applicationConfig: ApplicationConfig,
    private val joarkClient: JoarkClient = JoarkClient(applicationConfig),
) {
    suspend fun sendPdfTilJoark(
        pdf: ByteArray,
        dokumentJSON: SøknadDTO,
        vedlegg: List<Vedlegg>,
        callId: String,
    ): String {
        val journalpost = Journalpost.Søknadspost.from(
            fnr = dokumentJSON.personopplysninger.ident,
            søknadDTO = dokumentJSON,
            pdf = pdf,
            vedlegg = vedlegg,
        )
        return joarkClient.opprettJournalpost(journalpost, callId, forsoekFerdigstill = false)
    }
}
