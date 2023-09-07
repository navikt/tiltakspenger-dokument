package no.nav.tiltakspenger.dokument.brev

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import mu.KotlinLogging
import no.nav.tiltakspenger.dokument.søknad.SøknadService
import no.nav.tiltakspenger.domene.brev.BrevInnhold

val log = KotlinLogging.logger { }
fun Route.søknadRoutes(søknadService: SøknadService) {
    post("/brev") {
        val brev = call.receive<BrevInnhold>()

        call.respond(status = HttpStatusCode.OK, message = "")
    }
}
