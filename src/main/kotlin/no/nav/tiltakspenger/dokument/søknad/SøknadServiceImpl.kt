package no.nav.tiltakspenger.dokument.søknad

import io.ktor.http.content.MultiPartData
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import no.nav.tiltakspenger.dokument.deserialize
import no.nav.tiltakspenger.dokument.joark.JoarkService
import no.nav.tiltakspenger.dokument.pdfgen.sjekkContentType
import no.nav.tiltakspenger.soknad.api.pdf.PdfService

class SøknadServiceImpl(
    private val pdfService: PdfService,
    private val joarkService: JoarkService,
) : SøknadService {
    override suspend fun arkiverIJoark(søknad: SøknadDTO, vedlegg: List<Vedlegg>, callId: String): String {
        val søknadPDF = pdfService.lagSøknadPdf(søknad)
        val vedleggSomPdfer = pdfService.konverterVedlegg(vedlegg)

        return joarkService.sendSøknadPdfTilJoark(pdf = søknadPDF, søknadDTO = søknad, vedlegg = vedleggSomPdfer, callId = callId)
    }

    override suspend fun taInnSøknadSomMultipart(søknadSomMultipart: MultiPartData): Pair<SøknadDTO, List<Vedlegg>> {
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

    private fun PartData.FormItem.toSøknadDTO(): SøknadDTO {
        return deserialize(this.value)
    }

    private fun PartData.FileItem.toVedlegg(): Vedlegg {
        val filnavn = this.originalFileName ?: "untitled-${this.hashCode()}"
        val fileBytes = this.streamProvider().readBytes()
        return Vedlegg(filnavn = filnavn, contentType = sjekkContentType(fileBytes), dokument = fileBytes)
    }
}
