# Known limitations

- Ampacity tables are simplified reference values for common conditions —
  app shows a disclaimer; verify against C 15-100 / IEC 60364 before installation.
- Fixed voltages 220V mono / 380V tri (Sonelgaz standard); no custom voltage input.
- No instrumented tests in CI (requires emulator/device) — engine is JVM-tested.
- No release signing configured; CI ships debug APK only.
- No cloud sync, no photos in fiches, French/Arabic UI strings are minimal
  (calculator labels partly French-only in v1).
- No Play Store publication yet.
