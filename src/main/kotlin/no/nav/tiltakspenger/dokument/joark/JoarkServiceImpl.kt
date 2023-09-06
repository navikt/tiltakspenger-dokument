package no.nav.tiltakspenger.soknad.api.joark

import no.nav.tiltakspenger.dokument.søknad.SøknadDTO
import no.nav.tiltakspenger.dokument.søknad.Vedlegg

class JoarkServiceImpl(
    private val joark: Joark,
) : JoarkService {
    override suspend fun sendPdfTilJoark(
        pdf: ByteArray,
        søknadDTO: SøknadDTO,
        vedlegg: List<Vedlegg>,
        callId: String,
    ): String {
        val journalpost = Journalpost.Søknadspost.from(
            fnr = søknadDTO.personopplysninger.ident,
//            saksnummer = "",
            søknadDTO = søknadDTO,
            pdf = pdf,
            vedlegg = vedlegg,
        )
        return joark.opprettJournalpost(journalpost, callId)
    }
}
