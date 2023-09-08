package no.nav.tiltakspenger.dokument.søknad

import io.ktor.http.HttpStatusCode
import io.ktor.http.content.MultiPartData
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.application.call
import io.ktor.server.plugins.callid.callId
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import mu.KotlinLogging
import no.nav.tiltakspenger.dokument.deserialize
import no.nav.tiltakspenger.dokument.pdfgen.sjekkContentType

val log = KotlinLogging.logger { }
fun Route.søknadRoutes(søknadService: SøknadService) {
    post("/soknad") {
        val (søknadDTO, vedlegg) = taInnSøknadSomMultipart(call.receiveMultipart())
        val journalpostId = søknadService.arkiverIJoark(søknadDTO, vedlegg, call.callId!!)

        val joarkResponse = JoarkResponse(
            journalpostId = journalpostId,
            innsendingTidspunkt = søknadDTO.innsendingTidspunkt,
        )

        call.respond(status = HttpStatusCode.OK, message = joarkResponse)
    }
}

suspend fun taInnSøknadSomMultipart(søknadSomMultipart: MultiPartData): Pair<SøknadDTO, List<Vedlegg>> {
    lateinit var søknadDTO: SøknadDTO
    val vedleggListe = mutableListOf<Vedlegg>()
    søknadSomMultipart.forEachPart { part ->
        when (part) {
            is PartData.FormItem -> {
                søknadDTO = part.toSøknadDTO()
            }

            is PartData.FileItem -> {
                vedleggListe.add(part.toVedlegg())
            }

            else -> {}
        }
        part.dispose()
    }

    return Pair(søknadDTO, vedleggListe)
}

fun PartData.FormItem.toSøknadDTO(): SøknadDTO {
    return deserialize(this.value)
}

fun PartData.FileItem.toVedlegg(): Vedlegg {
    val filnavn = this.originalFileName ?: "untitled-${this.hashCode()}"
    val fileBytes = this.streamProvider().readBytes()
    return Vedlegg(filnavn = filnavn, contentType = sjekkContentType(fileBytes), dokument = fileBytes)
}
