# LifeOS — AI-First Multi-Module Clean Architecture Android App

**LifeOS** is an intelligent personal life operating system for Android built with modern **Clean Architecture**, **MVVM / MVI Unidirectional Data Flow**, **100% Jetpack Compose (Material 3)**, and **AI-First Tool-Calling Agentic design**.

It consolidates high-impact day-to-day tools into an extensible hub and empowers AI code agents (Antigravity, Cursor, Claude, GitHub Copilot) to reliably contribute new features without architectural decay.

---

## 🌟 Features Included

1. **Intelligent Hub & Daily Briefing (`:feature:dashboard`)**:
   - Executive AI morning briefing consolidating upcoming tasks, market exposures, and travel schedules.
   - Live metrics: Pending high-priority items, realized PnL, active study goals, upcoming trip countdown.
2. **Smart Reminders (`:feature:reminders`)**:
   - Natural language task scheduling and automated priority sorting.
   - Exposes `create_reminder` AI tool to the Copilot.
3. **Trading Journal & Emotional Review (`:feature:tradingjournal`)**:
   - Track stock/crypto positions, entry/exit prices, stop-loss, take-profit, and risk-reward ratios (R:R).
   - Tracks emotional trading psychology (FOMO, Revenge Trading, Calm, Confident).
   - Exposes `analyze_trading` AI tool.
4. **Active Learning & Syllabus Roadmaps (`:feature:learning`)**:
   - Structured skill roadmaps with progress tracking.
   - Exposes `create_learning_syllabus` AI tool.
5. **Travel Planner & Itinerary Builder (`:feature:travel`)**:
   - Day-by-day itineraries, estimated budgets, and essential packing checklists.
   - Exposes `generate_travel_plan` AI tool.
6. **LifeOS AI Copilot (`:feature:assistant`)**:
   - Conversational AI agent capable of invoking capabilities across any registered feature module through structured tool calling.

---

## 🏗️ Architecture & Module Map

LifeOS follows strict **Clean Architecture** with multi-module isolation:

```text
LifeOS/
├── app/                      # Application entry point, Hilt wiring, BottomNav & NavHost
├── core/
│   ├── common/               # Dispatchers, Result monad, DateTime utilities
│   ├── model/                # Pure Kotlin domain data classes (no Android dependencies)
│   ├── database/             # Room DB, entities, DAOs, converters
│   ├── network/              # AI network clients & offline-first simulated intelligence
│   ├── ai/                   # AI orchestrator, AiTool contracts, tool-calling registry
│   ├── designsystem/         # Compose Material 3 theme, components, typography, colors
│   └── navigation/           # Type-safe navigation destination contracts
├── feature/
│   ├── dashboard/            # Overview hub & AI daily briefing
│   ├── reminders/            # Task scheduling & priority management
│   ├── tradingjournal/       # Trading log & psychology analysis
│   ├── learning/             # Active recall syllabi & goals
│   ├── travel/               # Itineraries, packing checklists & budgets
│   └── assistant/            # Dedicated LifeOS Copilot AI chat
└── architecture-tests/       # Konsist test suite enforcing Clean Architecture guardrails
```

---

## 🤖 AI Code Agent Steering Files

This repository is optimized for autonomous and assisted AI pair programmers:

- **`AGENTS.md`**: Master steering document containing architectural contracts, module dependency hierarchies, domain purity laws, and conventions.
- **`.cursorrules`**: Context file for Cursor and agentic IDEs.
- **`.github/prompts/NEW_FEATURE_MODULE.md`**: Standard prompt template for asking AI to add a new life tool.
- **`tools/scaffold_feature.ps1`**: Automated PowerShell script to scaffold a new feature module in seconds:
  ```powershell
  .\tools\scaffold_feature.ps1 -Name "fitness"
  ```

---

## 🛡️ Code Quality & Architectural Enforcement

### Konsist Architectural Tests
Guarantees domain layer purity and layer conventions:
```bash
./gradlew :architecture-tests:test
```
Rules verified:
- Domain layer has **zero** Android framework imports (`android.*`).
- All ViewModels reside in `presentation` package and inherit `androidx.lifecycle.ViewModel`.
- All UseCases reside in `domain.usecase` and have a single public `operator fun invoke`.
- Repositories reside in `domain.repository` with implementations in `data.repository`.

### Detekt Static Code Analysis & Baselines
Configured in `config/detekt/detekt.yml` with Jetpack Compose support:
```bash
# Run code analysis across all modules
./gradlew detekt

# Generate / update Detekt baselines to suppress legacy or accepted issues
./gradlew detektBaseline
```

---

## 🚀 Building & Running

### Requirements
- JDK 17 (e.g. `C:\Users\vipin\.jdks\ms-17.0.16`)
- Android SDK 35 (installed in `%LOCALAPPDATA%\Android\Sdk`)
- Gradle 8.11 (via included wrapper `./gradlew`)

### Build Commands
```bash
# Compile and check all modules
./gradlew assembleDebug

# Run Konsist architecture guardrails
./gradlew :architecture-tests:test

# Run Detekt quality checks
./gradlew detekt
```
