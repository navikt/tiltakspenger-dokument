package no.nav.tiltakspenger.dokument.joark

import io.ktor.client.HttpClient
import io.ktor.server.config.ApplicationConfig
import no.nav.tiltakspenger.dokument.auth.ClientConfig
import no.nav.tiltakspenger.dokument.httpClientWithRetry

class JoarkCredentialsClient(
    config: ApplicationConfig,
    httpClient: HttpClient = httpClientWithRetry(timeout = 10L),
) {
    val joarkScope = config.property("scope.joark").getString()
    private val oauth2CredentialsClient = checkNotNull(ClientConfig(config, httpClient).clients["azure"])

    suspend fun getToken(): String {
        val clientCredentialsGrant = oauth2CredentialsClient.clientCredentials(joarkScope)
        return clientCredentialsGrant.accessToken
    }
}
