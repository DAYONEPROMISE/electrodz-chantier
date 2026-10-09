# Architecture

Single-module native app (`dz.electrodz.chantier`).

- `engine/` — pure-Kotlin calculation engine (no Android deps), JVM unit-tested.
- `data/` — Room entities/DAOs/database (`clients`, `calculations`, `fiches`).
- `ui/` — `AppViewModel` (AndroidViewModel, StateFlow) + Compose screens
  (Calculator, Fiches, Clients, History) + `PdfExport` (PrintedPdfDocument + FileProvider).
- `MainActivity` — Material3 theme, bottom-tab NavHost.

No network layer, no DI framework, no backend. Min SDK 26, target/compile 34,
Kotlin 1.9.24, AGP 8.5.2, Compose BOM 2024.06.00, Room 2.6.1.
