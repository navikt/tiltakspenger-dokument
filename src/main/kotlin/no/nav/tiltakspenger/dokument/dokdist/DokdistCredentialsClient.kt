package no.nav.tiltakspenger.dokument.dokdist

import io.ktor.client.HttpClient
import io.ktor.server.config.ApplicationConfig
import no.nav.tiltakspenger.dokument.auth.ClientConfig
import no.nav.tiltakspenger.dokument.httpClientWithRetry

class DokdistCredentialsClient(
    config: ApplicationConfig,
    httpClient: HttpClient = httpClientWithRetry(timeout = 10L),
) {
    private val dokdistScope = config.property("scope.dokdist").getString()
    private val oauth2CredentialsClient = checkNotNull(ClientConfig(config, httpClient).clients["azure"])

    suspend fun getToken(): String {
        val clientCredentialsGrant = oauth2CredentialsClient.clientCredentials(dokdistScope)
        return clientCredentialsGrant.accessToken
    }
}
