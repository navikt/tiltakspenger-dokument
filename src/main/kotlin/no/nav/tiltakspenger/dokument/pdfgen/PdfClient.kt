package no.nav.tiltakspenger.dokument.pdfgen

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.content.ByteArrayContent
import io.ktor.http.contentType
import io.ktor.server.config.ApplicationConfig
import no.nav.tiltakspenger.dokument.objectMapper
import no.nav.tiltakspenger.dokument.pdfgen.Detect.APPLICATON_PDF
import no.nav.tiltakspenger.dokument.pdfgen.Detect.IMAGE_JPEG
import no.nav.tiltakspenger.dokument.pdfgen.Detect.IMAGE_PNG
import no.nav.tiltakspenger.dokument.pdfgen.Detect.detect
import no.nav.tiltakspenger.dokument.søknad.SøknadDTO
import no.nav.tiltakspenger.dokument.søknad.Vedlegg
import no.nav.tiltakspenger.domene.brev.BrevDTO
import no.nav.tiltakspenger.soknad.api.pdf.PdfGenerator
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.UUID

internal const val pdfgenPath = "api/v1/genpdf/tpts"
internal const val pdfgenImagePath = "api/v1/genpdf/image/tpts"
internal const val SOKNAD_TEMPLATE = "soknad"
internal const val BREV_TEMPLATE = "vedtakInnvilgelse"

class PdfClient(
    config: ApplicationConfig,
    private val client: HttpClient,
) : PdfGenerator {
    private val log: Logger = LoggerFactory.getLogger(this::class.java)
    private val pdfEndpoint = config.property("endpoints.pdf").getString()

    override suspend fun genererSøknadPdf(søknadDTO: SøknadDTO): ByteArray {
        try {
            log.info("Starter generering av søknadspdf")
            return client.post("$pdfEndpoint/$pdfgenPath/$SOKNAD_TEMPLATE") {
                accept(ContentType.Application.Json)
                header("X-Correlation-ID", UUID.randomUUID())
                contentType(ContentType.Application.Json)
                setBody(objectMapper.writeValueAsString(søknadDTO))
            }.body()
        } catch (throwable: Throwable) {
            log.error("Kallet til pdfgen feilet $throwable")
            throw RuntimeException("Kallet til pdfgen feilet $throwable")
        }
    }

    override suspend fun genererBrevPdf(brevDTO: BrevDTO): ByteArray {
        try {
            log.info("Starter generering av vedtaksbrevpdf")
            return client.post("$pdfEndpoint/$pdfgenPath/$BREV_TEMPLATE") {
                accept(ContentType.Application.Json)
                header("X-Correlation-ID", UUID.randomUUID())
                contentType(ContentType.Application.Json)
                setBody(objectMapper.writeValueAsString(brevDTO))
            }.body()
        } catch (throwable: Throwable) {
            log.error("Kallet til pdfgen feilet $throwable")
            throw RuntimeException("Kallet til pdfgen feilet $throwable")
        }
    }

    override suspend fun konverterVedlegg(vedlegg: List<Vedlegg>): List<Vedlegg> {
        return vedlegg.map {
            log.info("Starter konvertering av vedlegg: ${it.filnavn}")
            val contentType = it.dokument.detect()
            when (contentType) {
                APPLICATON_PDF -> {
                    log.info("Oppdaget PDF-vedlegg, konverterer til bilde")
                    val bilder = PdfTools.konverterPdfTilBilder(it.dokument)
                    log.info("Konverterer bilder tilbake til PDF")
                    val enkeltsider = bilder.map { bilde ->
                        genererPdfFraBilde(Bilde(ContentType.Image.PNG, bilde.data))
                    }
                    val resultatPdf = PdfTools.slåSammenPdfer(enkeltsider)
                    Vedlegg(it.filnavn, "application/pdf", resultatPdf)
                }
                IMAGE_PNG -> {
                    log.info("Oppdaget PNG-vedlegg, konverterer til PDF")
                    val pdfFraBilde = genererPdfFraBilde(Bilde(ContentType.Image.PNG, it.dokument))
                    Vedlegg("$${it.filnavn}-konvertert.pdf", "application/pdf", pdfFraBilde)
                }
                IMAGE_JPEG -> {
                    log.info("Oppdaget JPEG-vedlegg, konverterer til PDF")
                    val pdfFraBilde = genererPdfFraBilde(Bilde(ContentType.Image.JPEG, it.dokument))
                    Vedlegg("$${it.filnavn}-konvertert.pdf", "application/pdf", pdfFraBilde)
                }
                else -> {
                    throw UnsupportedContentException("Ugyldig filformat")
                }
            }
        }
    }
    private suspend fun genererPdfFraBilde(bilde: Bilde): ByteArray {
        try {
            return client.post("$pdfEndpoint/$pdfgenImagePath") {
                accept(ContentType.Application.Json)
                header("X-Correlation-ID", UUID.randomUUID())
                contentType(bilde.type)
                setBody(ByteArrayContent(bilde.data))
            }.body()
        } catch (throwable: Throwable) {
            log.error("Kallet til pdfgen feilet $throwable")
            throw RuntimeException("Kallet til pdfgen feilet $throwable")
        }
    }
}
