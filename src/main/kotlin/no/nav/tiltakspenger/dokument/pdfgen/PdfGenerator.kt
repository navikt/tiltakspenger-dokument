package no.nav.tiltakspenger.soknad.api.pdf

import no.nav.tiltakspenger.dokument.søknad.SøknadDTO
import no.nav.tiltakspenger.dokument.søknad.Vedlegg

interface PdfGenerator {
    suspend fun genererSøknadPdf(søknadDTO: SøknadDTO): ByteArray
    suspend fun konverterVedlegg(vedlegg: List<Vedlegg>): List<Vedlegg>
}
