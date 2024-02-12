package no.nav.tiltakspenger.dokument.dokdist

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.config.ApplicationConfig
import no.nav.tiltakspenger.dokument.httpClientWithRetry
import no.nav.tiltakspenger.dokument.objectMapper
import org.slf4j.LoggerFactory

internal const val dokdistPath = "rest/v1/distribuerjournalpost"
class DokdistClient(
    private val config: ApplicationConfig,
    private val client: HttpClient = httpClientWithRetry(timeout = 30L),
    private val dokdistCredentialsClient: DokdistCredentialsClient = DokdistCredentialsClient(config),
) {

    private val log = LoggerFactory.getLogger(this::class.java)
    private val dokdistEndpoint = config.property("endpoints.dokdist").getString()

    suspend fun distribuerJournalpost(
        dokdistDTO: DokdistDTO,
        callId: String,
    ): String {
        try {
            log.info("Starter distribusjon av journalpost")
            val token = dokdistCredentialsClient.getToken()
            val res = client.post("$dokdistEndpoint/$dokdistPath") {
                accept(ContentType.Application.Json)
                header("X-Correlation-ID", "IND")
                header("Nav-Callid", callId)
                bearerAuth(token)
                contentType(ContentType.Application.Json)
                setBody(objectMapper.writeValueAsString(dokdistDTO))
            }

            when (res.status) {
                HttpStatusCode.OK -> {
                    val response = res.call.body<DokdistResponse>()
                    log.info(response.toString())

                    val bestillingId = response.bestillingId.ifEmpty {
                        log.error("Kallet til Dokdist gikk ok, men vi fikk ingen bestillingsId")
                        throw IllegalStateException("Kallet til Dokdist gikk ok, men vi fikk ingen bestillingsId")
                    }

                    log.info("Vi har distribuert journalpost med id : ${dokdistDTO.journalpost} og fikk bestillingsid $bestillingId")
                    return bestillingId
                }

                HttpStatusCode.BadRequest -> {
                    log.info("Ugyldig input. Validering av request body, eller validering av journalposten som journalpostId ${dokdistDTO.journalpost} refererer til feilet.}")
                    return "Ugyldig input for dokumentdistribusjon av journalpost med id: ${dokdistDTO.journalpost}. ${res.status}"
                }

                HttpStatusCode.NotFound -> {
                    log.info("Bruker mangler tilgang for å vise journalposten eller ugyldig OIDC token. ${dokdistDTO.journalpost}")
                    return "Bruker mangler tilgang for å vise journalposten eller ugyldig OIDC token. ${dokdistDTO.journalpost}. ${res.status}"
                }

                HttpStatusCode.Unauthorized -> {
                    log.info("Journalposten med id: ${dokdistDTO.journalpost} ble ikke funnet.")
                    return "Journalposten med id: ${dokdistDTO.journalpost} ble ikke funnet. ${res.status}"
                }

                HttpStatusCode.Conflict -> {
                    log.info("Journalposten med id: ${dokdistDTO.journalpost} er allerede distribuert.")
                    return "Journalposten med id: ${dokdistDTO.journalpost} er allerede distribuert. ${res.status}"
                }

                HttpStatusCode.Gone -> {
                    log.info("Journalposten med id: ${dokdistDTO.journalpost} kan ikke distribueres. Bruker er død og har ukjent adresse.")
                    return "Journalposten med id: ${dokdistDTO.journalpost} kan ikke distribueres. Bruker er død og har ukjent adresse. ${res.status}"
                }

                HttpStatusCode.InternalServerError -> {
                    log.info("Teknisk feil under prosessering av forsendelse med journalpostId: ${dokdistDTO.journalpost}.")
                    return "Teknisk feil under prosessering av forsendelse med journalpostId: ${dokdistDTO.journalpost}. ${res.status}"
                }

                else -> {
                    log.error("Kallet til dokdist feilet ${res.status} ${res.status.description}")
                    throw RuntimeException("Feil i kallet til dokdist")
                }
            }
        } catch (throwable: Throwable) {
            if (throwable is IllegalStateException) {
                throw throwable
            } else {
                log.error("Kallet til dokdist feilet $throwable")
                throw RuntimeException("Feil i kallet til dokdist $throwable")
            }
        }
    }
}
