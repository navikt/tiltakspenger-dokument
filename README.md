tiltakspenger
================

Støtteverktøy til oppretting og utsending av dokumenter i Tiltakspenger.

# Komme i gang
## Forutsetninger
- [JDK](https://jdk.java.net/)
- [Kotlin](https://kotlinlang.org/)
- [Gradle](https://gradle.org/) brukes som byggeverktøy og er inkludert i oppsettet

For hvilke versjoner som brukes, [se byggefilen](build.gradle.kts)

## Bygging og denslags
For å bygge artifaktene:

```sh
./gradlew build
```

### Kjøre opp appen lokalt

For å kjøre opp tiltakspenger-vedtak lokalt fra et IDE som for eksempel IntelliJ, kan man kjøre opp `main`-funksjonen
som ligger i `Application.kt` ([link](src/main/kotlin/no/nav/tiltakspenger/dokument/Application.kt)).

For at det skal funke å kjøre opp appen fra IntelliJ eller tilsvarende IDE må man sette opp noen miljøvariabler. I IntelliJ kan
de konfigureres opp i relevant Run Configuration som blir lagd når man kjører opp Application.kt for første gang.

Miljøvariabler som må settes (be om hjelp av en annen utvikler på teamet til å få satt riktige miljøvariabler på din maskin):
```
AZURE_APP_CLIENT_ID=
AZURE_APP_CLIENT_SECRET=
AZURE_APP_WELL_KNOWN_URL=
DOKDIST_ENDPOINT_URL=
DOKDIST_SCOPE=
JOARK_ENDPOINT_URL=
JOARK_SCOPE=
PDF_ENDPOINT_URL=
PORT= (default er 8080, men bør endres hvis man kjører opp flere apper lokalt)
```

**OBS!** `tiltakspenger-dokument` er avhengig av at man har en større verdikjede kjørende i miljø for å kunne kjøres opp
lokalt, f.eks. feks Kafka, pdfgen, og mock av joark og dokdist. Man anbefales å se i [meta-repoet for tiltakspenger](https://github.com/navikt/tiltakspenger)
for hvordan man kan få kjørt opp hele verdikjeden, inkl. `tiltakspenger-dokument` i docker-containere.


---

# Henvendelser

Spørsmål knyttet til koden eller prosjektet kan stilles som issues her på GitHub.

## For NAV-ansatte

Interne henvendelser kan sendes via Slack i kanalen #tiltakspenger-værsågod.
