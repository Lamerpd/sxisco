# sxisco

A native Android app that shows every running process on your device and drops you into a real root shell scoped to whichever app you tap — for debugging, poking around `/proc`, and general low-level tinkering. **Requires root.**

<p align="left">
  <img src="https://github.com/Lamerpd/sxisco/actions/workflows/build.yml/badge.svg" alt="Build status" />
  <img src="https://img.shields.io/badge/Kotlin-2.3.21-7F52FF?logo=kotlin&logoColor=white" alt="Kotlin" />
  <img src="https://img.shields.io/badge/Jetpack%20Compose-BOM%202026.06.00-4285F4?logo=jetpackcompose&logoColor=white" alt="Jetpack Compose" />
  <img src="https://img.shields.io/badge/minSdk-26-brightgreen" alt="Min SDK 26" />
  <img src="https://img.shields.io/badge/Root-Required-critical" alt="Root required" />
</p>

## What it does

- **Processos** — lists every app currently running, read straight from `/proc` (needs root, since Android hides other apps' `/proc/<pid>` from unprivileged processes since Android 11).
- **Terminal** — tap any app in the list and get a live root shell, with that app's package name and PID shown as context. Every command you type runs for real against a persistent `su` session — not a sandboxed simulation.
- **Packages** — placeholder for now; a real package browser/installer (`dpkg`/`apt`-style) is a planned follow-up, not wired up yet.
- **Ajustes** — basic app info and root status.
- Looping background music with a mute/unmute toggle.

## Tech stack

- Kotlin + Jetpack Compose (Material 3), no XML layouts
- A persistent `su` session (`Runtime.exec("su")`) for root command execution
- `PackageManager` + `/proc` scanning to resolve running processes back to installed apps
- Gradle Kotlin DSL with a version catalog (`gradle/libs.versions.toml`)
- Built and packaged via GitHub Actions (`.github/workflows/build.yml`) — no local Android SDK required to get a debug APK

## Requirements

- A rooted Android device (Magisk, KernelSU, or similar) — the app is non-functional without root
- Android 8.0 (API 26) or newer

## Project structure

```
app/src/main/java/com/sxisco/app/
├── MainActivity.kt          # navigation, root session lifecycle, music player
├── core/
│   ├── RootShell.kt         # persistent su session (start/exec/close)
│   ├── ProcessScanner.kt    # /proc scan → PackageManager resolution
│   └── BackgroundMusicPlayer.kt
├── data/
│   └── RunningProcess.kt
└── ui/
    ├── theme/                # colors, typography, Material3 theme
    └── screens/              # HomeScreen, TerminalScreen, PackagesScreen, SettingsScreen
```

## Building

Every push to `main` triggers a GitHub Actions build that assembles a debug APK — grab it from the **Artifacts** section of the latest successful run under the **Actions** tab.

To build locally, you'll need Android Studio (or the command-line SDK) with `compileSdk 36` / `build-tools 36` installed, then:

```bash
./gradlew assembleDebug
```

## Documentation & references

- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Kotlin](https://kotlinlang.org/docs/home.html)
- [Android Gradle Plugin release notes](https://developer.android.com/build/releases/gradle-plugin)
- [Gradle](https://docs.gradle.org)
- [Material Design 3](https://m3.material.io/)
- [Magisk](https://topjohnwu.github.io/Magisk/)
- [KernelSU](https://kernelsu.org/)

## Credits

- UI/UX structure inspired by [Shizuku](https://github.com/RikkaApps/Shizuku) (RikkaApps)
- App mascot hand-drawn by the project author
- Development assisted by [Claude](https://claude.com) (Anthropic)

## License

Not yet decided — all rights reserved by default until a license is chosen.
