# Testing

- `gradle :app:testDebugUnitTest` — 8 JUnit tests on `ElectroEngine`
  (mono/tri current, lighting step-up 2.5→4mm², breaker series, overload
  graceful failure, input validation, Cu vs Al).
- `gradle :app:lintDebug` — Android lint, must pass.
- `gradle :app:assembleDebug` — compilation gate.
- Instrumented/UI tests: NOT RUN (no emulator in CI; Room DAO + navigation tests
  require a device — see KNOWN_LIMITATIONS).
- Manual checklist: calculator reference cases, CRUD offline, PDF share,
  FR/AR switch + RTL, airplane-mode run.

Results table is published in the delivery report after the first CI run.
