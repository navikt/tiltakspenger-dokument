package no.nav.tiltakspenger.dokument.pdfgen

import no.nav.tiltakspenger.dokument.søknad.SøknadDTO
import no.nav.tiltakspenger.dokument.søknad.Vedlegg
import no.nav.tiltakspenger.libs.dokument.BrevDTO

interface PdfGenerator {
    suspend fun genererSøknadPdf(søknadDTO: SøknadDTO): ByteArray
    suspend fun genererBrevPdf(brevDTO: BrevDTO): ByteArray
    suspend fun konverterVedlegg(vedlegg: List<Vedlegg>): List<Vedlegg>
}
