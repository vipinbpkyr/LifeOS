# AI Prompt Template: Adding a New Life Tool Feature Module

Copy and paste this prompt to an AI agent when you want to add a new tool (e.g. Budget/Finance, Habits, Sleep/Health, Books/Reading):

```markdown
I want to add a new life tool feature module named `:feature:<FEATURE_NAME>` to LifeOS.

Context & Requirements:
1. Feature Purpose: <Describe what this tool does, e.g. "Track daily water and calorie intake with AI meal logging">
2. Layers to generate:
   - Domain:
     - Entity model in `:core:model` (or local domain model)
     - Repository interface `interface <Name>Repository`
     - UseCases: `Get<Name>sUseCase`, `Save<Name>UseCase`, `Analyze<Name>WithAiUseCase`
   - Data:
     - Entity & DAO in `:core:database`
     - Repository implementation `<Name>RepositoryImpl`
     - Hilt DI module
   - Presentation:
     - Compose M3 Screen `<Name>Screen.kt`
     - `<Name>ViewModel.kt` with `<Name>UiState`
   - AI Tool Calling:
     - Expose an AI Tool definition in `:core:ai` so the LifeOS Copilot can query or manipulate this feature.
   - Navigation:
     - Add route to `:core:navigation` and wire into `LifeOsNavHost.kt`.
3. Architecture Guardrails:
   - Ensure domain layer has ZERO Android dependencies (verified by Konsist).
   - Ensure Detekt naming and formatting compliance.
```
