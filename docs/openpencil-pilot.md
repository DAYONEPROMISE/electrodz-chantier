# OpenPencil pilot — electrodz-chantier

Fully-automatic UX pass, no manual design step.

## OpenPencil artifacts (Shared/OpenPencil/electrodz/)
- `design.html` — improved calculator + result + empty-state screens in DZ tokens
- `design.fig` — imported via `openpencil import` (1 page, 44 nodes: 21 FRAME, 23 TEXT)
- `lint --preset accessibility` — 0 errors, 0 warnings
- `analyze colors` — palette confirmed (#006A60 primary, #FBFDF9 surface, #C1C9BF outline-variant, …)
- `preview.svg` — page export

## Code changes (this commit)
- NEW `ui/DzTheme.kt` — DZ 2026 light/dark M3 schemes (primary #006A60 / #4DDAD0, …) +
  dynamic-color on API 31+ with token fallback + `DzSectionTitle` / `DzEmptyState` helpers.
- `MainActivity.kt` — wrapped in `DzTheme`, added `TopAppBar`, nav icons now carry
  `contentDescription` (TalkBack fix).

## APK
Push to `main` triggers `.github/workflows/android-build.yml`
(tests + lint + assembleDebug). APK under Actions → run → Artifacts (`electrodz-debug-apk`).

## Next (batch rollout)
Same 3-file pattern per repo: theme + TopAppBar/descriptions + this doc.
AR strings (`values-ar`) + empty/error states per screen follow after green build.
