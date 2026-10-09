# ElectroDZ Chantier — MVP Specification

## Product name
ElectroDZ Chantier

## Target users
Independent electricians, small BTP electrical lots, maintenance technicians in Algeria.
French- and Arabic-speaking, often offline on chantier.

## Main problem
Fast, reliable cable sizing (section, voltage drop, breaker) + simple intervention
paperwork, fully offline, in French/Arabic.

## Core workflow
1. Open app → 2. Size a cable (power, voltage, length, material) → 3. Save result
to history → 4. Attach it to an intervention fiche (client + site + tasks) →
5. Export/share PDF on site.

## Essential features (MVP)
- F1 Cable sizing: P(W), mono 220V / tri 380V, cos φ, length, Cu/Al, PVC/XLPE →
  current, min section by ampacity, voltage-drop check (3% lighting / 5% other),
  recommended section (standard series), breaker rating. Disclaimer shown.
- F2 Voltage-drop check for an existing section.
- F3 Intervention fiches CRUD (client, phone, wilaya, date, description, tasks done flags).
- F4 Client register CRUD.
- F5 Calculation history (Room), link calc → fiche.
- F6 PDF export via Android Print framework + share.
- F7 FR + AR (RTL) localisation. 100% offline. No account.

## Explicitly excluded
Invoicing/TVA/CNAS, cloud sync, authentication, subscriptions, AI, GPS, photos,
OBD/hardware integration, admin dashboard.

## Data model (Room)
Client(id, name, phone, wilaya, address, notes)
Calculation(id, ts, type, powerW, voltage, phases, cosPhi, lengthM, material,
insulation, usage, sectionMm2, currentA, dropPct, breakerA, verdict)
Fiche(id, clientId, date, siteAddress, wilaya, description, tasksJson, calcId?)

## Architecture
Single-module native app: Kotlin + Jetpack Compose + Material3 + MVVM
(ViewModel + StateFlow), Room, Coroutines, Navigation-Compose. Pure-Kotlin
`engine` package (no Android deps) for calculations → unit-testable on JVM.

## Permissions
None in MVP (no camera/location/internet). INTERNET permission NOT declared.

## Offline behavior
Everything local. No network calls. PDF generated on-device.

## Error handling
Invalid inputs blocked inline (range checks); Room ops in try/catch with user message;
PDF failure surfaces a message, never a crash.

## Testing strategy
JUnit unit tests on engine (current, drop, section selection, breaker, edge cases);
Room DAO instrumented tests declared but require device (NOT RUN locally);
lint + assembleDebug in CI.

## Acceptance criteria
- Correct sizing on reference cases (verified in unit tests).
- CRUD works offline; history persists across restart.
- PDF generates and shares.
- FR + AR UI, RTL correct.
- `./gradlew testDebugUnitTest lint assembleDebug` green in CI; APK artifact exists.
