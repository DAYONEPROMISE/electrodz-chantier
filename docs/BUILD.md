# Build

## CI (reproducible)
Push to GitHub → workflow `.github/workflows/android-build.yml` runs on
`ubuntu-latest`: JDK 17 (Temurin) + Android SDK + Gradle 8.7, then
`testDebugUnitTest`, `lintDebug`, `assembleDebug`, APK verification, artifact upload.

## Local
Requirements: JDK 17, Android SDK with platform 34 + build-tools 34.0.0,
`ANDROID_HOME` (or `ANDROID_SDK_ROOT`) set, Gradle 8.7 on PATH
(CI downloads it; locally install via package manager).

```sh
gradle :app:testDebugUnitTest
gradle :app:lintDebug
gradle :app:assembleDebug
# APK: app/build/outputs/apk/debug/app-debug.apk
```

No `local.properties` needed when `ANDROID_HOME` is set. No secrets required
for the debug build. Release signing (via GitHub Secrets) is NOT configured —
out of MVP scope.
