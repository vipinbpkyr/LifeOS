# AGENTS.md — AI Code Agent Architectural & Engineering Steering Guide

Welcome to the **LifeOS** codebase. This file is the primary steering specification for AI code agents (such as Antigravity, Claude, Cursor, ChatGPT, and GitHub Copilot) and human engineers working on this repository.

All AI agents **MUST** read, understand, and strictly follow these rules before proposing or generating code changes.

---

## 1. System Vision & Core Principles

**LifeOS** is an AI-First, offline-capable, modular life-management operating system on Android. It consolidates daily tools into an extensible personal intelligence platform:
- **Smart Reminders**: Natural language scheduling, context-aware prioritization.
- **Trading Journal**: Trade logging, PnL, risk-to-reward metrics, emotional sentiment tracking, AI post-trade review.
- **Continuous Learning**: Active recall goals, syllabus tracking, AI flashcard & quiz generation.
- **Travel Planner**: Destination itineraries, packing checklists, AI travel recommendations.
- **AI Copilot (LifeOS Assistant)**: Central intelligence hub with tool-calling capabilities that inspects and interacts with all life modules.

### Fundamental Principles
1. **Clean Architecture with Unidirectional Data Flow (UDF)**:
   - `Presentation` (Jetpack Compose + MVVM/MVI StateFlow)
   - `Domain` (Pure Kotlin Use Cases, Entity Models, Repository Interfaces — **ZERO** Android framework imports)
   - `Data` (Repository implementations, Room DAOs, Network services, Data mappers)
2. **AI-First Tool-Calling Design**:
   - Every feature module should expose AI-callable capabilities (Tools) via `:core:ai` so the global AI assistant can answer queries and execute actions on behalf of the user.
3. **Strict Module Boundary Isolation**:
   - Features **NEVER** depend on other features. Inter-feature navigation and data sharing happens through `:core:navigation`, `:core:model`, and `:core:ai`.
4. **Architectural Enforcement**:
   - `Konsist` verifies architectural rules automatically in CI and local tests.
   - `Detekt` enforces code style, cyclomatic complexity, and Kotlin formatting.

---

## 2. Multi-Module Hierarchy & Dependency Graph

```text
                       ┌──────────────────────┐
                       │        :app          │
                       └──────────┬───────────┘
                                  │ (wires navigation, Hilt graphs)
        ┌─────────────┬───────────┼───────────┬─────────────┐
        │             │           │           │             │
        ▼             ▼           ▼           ▼             ▼
┌──────────────┐┌───────────┐┌──────────┐┌──────────┐┌─────────────┐
│  :feature:   ││ :feature: ││ :feature:││:feature: ││  :feature:  │
│  dashboard   ││ reminders ││  trading ││ learning ││   travel    │
└───────┬──────┘└─────┬─────┘└────┬─────┘└────┬─────┘└──────┬──────┘
        │             │           │           │             │
        └─────────────┴─────┬─────┴───────────┴─────────────┘
                            ▼
                   ┌──────────────────┐
                   │    :feature:     │
                   │    assistant     │ (AI Copilot UI)
                   └────────┬─────────┘
                            │
        ┌───────────────────┼───────────────────┐
        ▼                   ▼                   ▼
┌───────────────┐   ┌───────────────┐   ┌───────────────┐
│   :core:ai    │   │:core:database │   │ :core:network │
└───────┬───────┘   └───────┬───────┘   └───────┬───────┘
        │                   │                   │
        └───────────────────┼───────────────────┘
                            ▼
┌────────────────────────────────────────────────────────┐
│                      :core:model                       │
│    (Pure Kotlin models: Reminder, Trade, Goal, etc.)   │
└───────────────────────────┬────────────────────────────┘
                            ▼
┌────────────────────────────────────────────────────────┐
│                      :core:common                      │
│     (Result/Resource monad, Coroutine Dispatchers)     │
└────────────────────────────────────────────────────────┘

Also shared across presentation layers:
┌──────────────────────┐        ┌───────────────────────┐
│ :core:designsystem   │        │   :core:navigation    │
│ (Compose Theme, M3)  │        │ (Type-safe nav routes)│
└──────────────────────┘        └───────────────────────┘
```

---

## 3. Layer Rules & Conventions

### 3.1 Domain Layer (`feature:<name>/src/main/java/.../domain/`)
- **Purity**: MUST be 100% pure Kotlin. **NEVER** import `android.*`, `androidx.*`, or Compose in domain packages (except `@Inject` for Hilt).
- **Models**: Business entities live in `:core:model` or local `domain/model`.
- **Repository Contracts**: Interfaces only (e.g., `interface ReminderRepository`).
- **Use Cases**:
  - Name as verbs ending in `UseCase` (e.g., `GetActiveRemindersUseCase`, `GenerateAiItineraryUseCase`).
  - Exactly **ONE** public method: `operator fun invoke(...)`.

### 3.2 Data Layer (`feature:<name>/src/main/java/.../data/`)
- Implements domain repository interfaces (e.g., `ReminderRepositoryImpl`).
- Maps between Room Entities (`core:database`) and Domain Models (`core:model`).
- Interacts with DAOs and Network API clients.
- Injected via Hilt in a feature `DataModule` using `@Binds`.

### 3.3 Presentation Layer (`feature:<name>/src/main/java/.../presentation/`)
- **ViewModel**:
  - Extends `androidx.lifecycle.ViewModel`.
  - Annotated with `@HiltViewModel`.
  - Exposes single `StateFlow<UiState>` via `.asStateFlow()`.
  - Exposes one-off events (navigation, toast) via `SharedFlow<UiEvent>` or `Channel`.
- **UiState**:
  - Immutable Kotlin `data class` with default values (e.g., `data class RemindersUiState(val isLoading: Boolean = false, ...)`).
- **Jetpack Compose UI**:
  - Screens must be stateless at the leaf level with hoisted state and lambda event callbacks.
  - Route composable retrieves ViewModel via `hiltViewModel()` and passes state down.

---

## 4. Step-by-Step: Adding a New Life Tool Feature

When requested to add a new tool (e.g., `:feature:fitness`, `:feature:finance`, `:feature:meditation`):

1. **Register in `settings.gradle.kts`**:
   ```kotlin
   include(":feature:myfeature")
   ```
2. **Create `build.gradle.kts`** applying plugins:
   - `alias(libs.plugins.android.library)`
   - `alias(libs.plugins.kotlin.android)`
   - `alias(libs.plugins.compose.compiler)`
   - `alias(libs.plugins.ksp)`
   - `alias(libs.plugins.hilt)`
   Dependencies: `:core:common`, `:core:model`, `:core:designsystem`, `:core:navigation`, `:core:database`, `:core:ai`.
3. **Define Domain Entities & Use Cases**:
   - Write repository interface.
   - Implement single-purpose UseCases with `operator fun invoke`.
4. **Implement Data Layer**:
   - Room Entity in `:core:database` (or feature-specific DAO).
   - Repository implementation with Flow-based reactive queries.
5. **Create Compose Presentation**:
   - `MyFeatureViewModel` with `MyFeatureUiState`.
   - `MyFeatureScreen` with M3 design components.
6. **Register Navigation & AI Tool**:
   - Add route in `:core:navigation`.
   - Register AI Tool capability in `:core:ai` so the AI assistant can interact with this feature!
7. **Verify Quality**:
   - Run `./gradlew konsist:test` and `./gradlew detekt`.

---

## 5. Konsist Architecture Guardrails

Konsist tests located in `:architecture-tests` guarantee:
- **No Android imports in Domain**: Any `import android.*` in `..domain..` causes build failure.
- **ViewModel naming and inheritance**: All ViewModels must end with `ViewModel` and inherit `androidx.lifecycle.ViewModel`.
- **Repository conventions**: Repository interfaces must reside in `domain` package and implementations in `data` package.
- **Use case single responsibility**: Use cases must have only one public operator method `invoke`.

---

## 6. Code Style & Detekt Requirements

- Always use trailing commas in multi-line parameters and arguments.
- Max cyclomatic complexity: 15 per function.
- Avoid wildcard imports (`import foo.bar.*` is forbidden).
- All coroutine scopes must inject `LifeOsDispatchers` (never hardcode `Dispatchers.IO` or `Dispatchers.Default`).
- Favor explicit type declarations on public repository/usecase APIs.
