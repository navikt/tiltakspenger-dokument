package no.nav.tiltakspenger.dokument.vedtak

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import mu.KotlinLogging
import java.time.LocalDate

val log = KotlinLogging.logger { }

fun Route.vedtakRoutes() {
    post("/dokument/vedtaksbrev") {
        val brevDTO = call.receive<BrevDTO>()
        log.info { "Motatt $brevDTO" }
        call.respond(status = HttpStatusCode.OK, message = "{}")
    }
}

data class BrevDTO(
    val vedtakId: String,
    val vedtaksdato: LocalDate,
    val vedtaksType: VedtaksTypeDTO,
    val periode: PeriodeDTO,
    val saksbehandler: String,
    val beslutter: String,
    val tiltak: List<TiltakDTO>,
)

enum class VedtaksTypeDTO(val navn: String, val skalSendeBrev: Boolean) {
    AVSLAG("Avslag", true),
    INNVILGELSE("Innvilgelse", true),
    STANS("Stans", true),
    FORLENGELSE("Forlengelse", true),
}

data class TiltakDTO(
    val periodeDTO: PeriodeDTO,
    val typeBeskrivelse: String,
    val typeKode: String,
    val antDagerIUken: Float,
)

data class PeriodeDTO(
    val fra: LocalDate,
    val til: LocalDate,
)
