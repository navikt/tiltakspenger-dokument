package no.nav.tiltakspenger.dokument.joark

import io.ktor.server.config.ApplicationConfig
import no.nav.tiltakspenger.dokument.meldekort.DokumentMeldekortDTO
import no.nav.tiltakspenger.dokument.søknad.SøknadDTO
import no.nav.tiltakspenger.dokument.søknad.Vedlegg
import no.nav.tiltakspenger.libs.dokument.BrevDTO

class JoarkServiceImpl(
    applicationConfig: ApplicationConfig,
    private val joarkClient: JoarkClient = JoarkClient(applicationConfig),
) : JoarkService {

    override suspend fun sendSøknadPdfTilJoark(
        pdf: ByteArray,
        søknadDTO: SøknadDTO,
        vedlegg: List<Vedlegg>,
        callId: String,
    ): String {
        val journalpost = Journalpost.Søknadspost(
            fnr = søknadDTO.personopplysninger.ident,
            søknadDTO = søknadDTO,
            pdf = pdf,
            vedlegg = vedlegg,
        )
        return joarkClient.opprettJournalpost(journalpost, callId, forsoekFerdigstill = false)
    }

    override suspend fun sendBrevPdfTilJoark(pdf: ByteArray, brevDTO: BrevDTO, callId: String): String {
        val journalpost = Journalpost.Brevpost(
            fnr = brevDTO.personalia.ident,
            brevDTO = brevDTO,
            pdf = pdf,
            saksId = brevDTO.saksnummer,
        )
        return joarkClient.opprettJournalpost(journalpost, callId, forsoekFerdigstill = true)
    }

    override suspend fun sendMeldekortJsonTilJoark(pdf: ByteArray, meldekortDTO: DokumentMeldekortDTO, callId: String): String {
        val journalpost = Journalpost.Meldekortpost(
            fnr = meldekortDTO.personopplysninger.ident,
            pdf = pdf,
            meldekortDTO = meldekortDTO,
            saksId = meldekortDTO.sakId,
        )
        return joarkClient.opprettJournalpost(journalpost, callId, forsoekFerdigstill = true)
    }
}
