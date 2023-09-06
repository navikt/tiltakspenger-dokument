package no.nav.tiltakspenger.soknad.api.pdf

import no.nav.tiltakspenger.dokument.søknad.SøknadDTO
import no.nav.tiltakspenger.dokument.søknad.Vedlegg

interface PdfService {
    suspend fun lagPdf(søknadDTO: SøknadDTO): ByteArray
    suspend fun konverterVedlegg(vedlegg: List<Vedlegg>): List<Vedlegg>
}
