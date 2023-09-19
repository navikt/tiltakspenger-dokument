package no.nav.tiltakspenger.dokument.søknad

interface SøknadService {
    suspend fun arkiverIJoark(søknad: SøknadDTO, vedlegg: List<Vedlegg>, callId: String): String
}
