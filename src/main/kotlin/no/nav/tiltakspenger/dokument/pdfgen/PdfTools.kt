package no.nav.tiltakspenger.dokument.pdfgen

import com.fasterxml.jackson.databind.JsonNode
import io.ktor.http.ContentType
import no.nav.tiltakspenger.dokument.objectMapper
import org.apache.pdfbox.io.MemoryUsageSetting
import org.apache.pdfbox.multipdf.PDFMergerUtility
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.rendering.PDFRenderer
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

// WIP

class PdfTools {
    companion object {
        fun konverterPdfTilBilder(pdfByteArray: ByteArray): List<Bilde> {
            val pdfDokument = PDDocument.load(pdfByteArray)
            val renderer = PDFRenderer(pdfDokument)
            val siderSomBilder = (0 until pdfDokument.numberOfPages).map {
                val bilde = renderer.renderImage(it)
                val baos = ByteArrayOutputStream()
                ImageIO.write(bilde, "png", baos)
                Bilde(ContentType.Image.PNG, baos.toByteArray())
            }
            pdfDokument.close()
            return siderSomBilder
        }

        fun slåSammenPdfer(pdfbaListe: List<ByteArray>): ByteArray {
            val pdfMerger = PDFMergerUtility()
            val baosUt = ByteArrayOutputStream()
            pdfMerger.destinationStream = baosUt
            pdfbaListe.forEach {
                pdfMerger.addSource(ByteArrayInputStream(it))
            }
            pdfMerger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly()); // TODO: Sjekk ut memory settings
            return baosUt.toByteArray()
        }

        fun genererPdfFraData(data: Any): ByteArray {
            objectMapper.writeValueAsString(data).let {
                val jsonNode: JsonNode = objectMapper.readTree(it)
                return genererPdfFraJson(jsonNode)
            }
        }

        private fun genererPdfFraJson(jsonNode: JsonNode): ByteArray {
            PDDocument().use { document ->
                val margin = 50f
                val linjeAvstand = 15f
                val sideStørrelse = PDPage().mediaBox.height - margin * 3f
                val linjerPerSide = (sideStørrelse / linjeAvstand).toInt()
                val peneLinjer = jsonNode.toPrettyString().split("\n")
                peneLinjer.chunked(linjerPerSide).forEach { linjer ->
                    val side = PDPage()
                    document.addPage(side)
                    PDPageContentStream(document, side).use { contentStream ->
                        contentStream.setFont(PDType1Font.HELVETICA, 12f)
                        contentStream.beginText()
                        contentStream.newLineAtOffset(margin, 700f)
                        linjer.forEach { linje ->
                            contentStream.showText(linje)
                            contentStream.newLineAtOffset(0f, -linjeAvstand)
                        }
                        contentStream.endText()
                    }
                }

                val byteArray: ByteArray = ByteArrayOutputStream().use { byteArrayOutputStream ->
                    document.save(byteArrayOutputStream)
                    byteArrayOutputStream.toByteArray()
                }
                return byteArray
            }
        }
    }
}
class Bilde(
    val type: ContentType,
    val data: ByteArray,
)
