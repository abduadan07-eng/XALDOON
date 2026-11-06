# XALDOON (Android App)
> *Every Problem Deserves a Solution* | *Dhibaato Kasta Waxay Mudan Tahay Xal*

**XALDOON** is an international problem-solving, services, skills, and opportunities platform built natively for Android using Kotlin and Jetpack Compose.

## 🌟 Key Highlights

- **100% Offline-First Architecture (Phase 1):** Powered by an on-device Room SQLite database. All posts, solutions, services, and saved bookmarks stay securely on your device with zero cloud tracking.
- **Bilingual Interface:** Instant switching between **English** and **Somali (Af-Soomaali)** without losing app state.
- **Material Design 3 (M3):** Emerald green primary brand identity, deep navy typography, and seamless light/dark theme toggle.
- **5 Core Ecosystem Tabs:**
  1. **Home:** Personalized greeting, category rail, recent needs, recent solutions, and featured opportunities.
  2. **Explore:** Keyword search across all content with category filters, content-type chips, and sorting.
  3. **Create:** Multi-category creation hub (Needs, Solutions, Services, Opportunities) with validation, editing, and deletion controls.
  4. **Opportunities:** Filter jobs, freelancing, business partnerships, education, and investment listings with deadline pills.
  5. **Profile:** Local stats, profile editing, language switcher, dark mode toggle, and Phase 2 company roadmap.
- **Future Backend Ready:** Structured contracts in `com.example.future` ready for future self-hosted PostgreSQL cloud synchronization, encrypted messaging, and escrow payments.

## 🛠️ Tech Stack

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose (Material 3)
- **Architecture:** Clean MVVM (Model-View-ViewModel + Repository Pattern)
- **Local Database:** Room Database (SQLite with KSP)
- **Min SDK:** 24 (Android 7.0+)
- **Target SDK:** 36

## 📦 Building the APK

To build the APK locally using Gradle:

```bash
# Debug APK
./gradlew assembleDebug

# Output APK path:
# app/build/outputs/apk/debug/app-debug.apk
```

---
© 2026 XALDOON Company. All rights reserved.
