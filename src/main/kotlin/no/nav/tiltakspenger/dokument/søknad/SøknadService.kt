package no.nav.tiltakspenger.dokument.søknad

import io.ktor.http.content.MultiPartData

interface SøknadService {
    suspend fun arkiverIJoark(søknad: SøknadDTO, vedlegg: List<Vedlegg>, callId: String): String
    suspend fun taInnSøknadSomMultipart(søknadSomMultipart: MultiPartData): Pair<SpørsmålsbesvarelserDTO, List<Vedlegg>>
}
