# ElectroDZ Chantier

Offline field toolkit for electricians in Algeria (French + Arabic RTL, 100% offline).

- Cable sizing (IEC 60364 / C 15-100 method): current, section by ampacity,
  voltage-drop check (3% lighting / 5% other), recommended section, breaker rating.
- Voltage-drop verification, calculation history (Room).
- Intervention fiches + client register + on-device PDF export/share.
- No account, no internet, no tracking.

> Results are indicative — verify against C 15-100 / IEC 60364 and Sonelgaz rules
> before any installation.

## Build

See [docs/BUILD.md](docs/BUILD.md). Quick: CI builds on every push
(`.github/workflows/android-build.yml`) and uploads the debug APK artifact.
Locally (JDK 17 + Android SDK 34): `gradle :app:assembleDebug`.

## Test

`gradle :app:testDebugUnitTest` — see [docs/TESTING.md](docs/TESTING.md).

## Permissions

None declared (no INTERNET). See [docs/KNOWN_LIMITATIONS.md](docs/KNOWN_LIMITATIONS.md).
