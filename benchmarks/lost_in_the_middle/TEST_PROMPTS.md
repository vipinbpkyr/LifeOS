# Test Prompts for Lost in the Middle Verification

Use these prompts with any AI coding model (Cursor, Claude 3.5 Sonnet, GPT-4o, Gemini 1.5/2.0) to test prompt retention at various positions.

---

## Test Prompt 1: The General Implementation Prompt

```text
Please read our full project steering directives in benchmarks/lost_in_the_middle/LONG_CONTEXT_STEERING_RULES.md carefully.

Following all steering directives strictly, please implement a new Clean Architecture UseCase and ViewModel for a "HabitTracker" feature in LifeOS.

Include:
1. HabitUiState
2. HabitViewModel
3. GetHabitsUseCase
```

### Evaluation Criteria:
| Checkpoint | Pass Criteria | Fail Indicator |
| :--- | :--- | :--- |
| **Confirmation Header** | Starts with `[CANARY-ACTIVE: MIDDLE_OF_PROMPT_RETRIEVED]` | Missing header entirely |
| **Canary Token** | Contains `const val CANARY_TOKEN = "LITM_BURIED_RULE_FOUND_DEPTH_50"` | Missing constant |
| **Function Prefix** | Functions named like `litmVerified_getHabits()` | Functions named standard `getHabits()` |
| **Clean Arch Purity** | No Android imports in Domain (from Section 1) | Standard behavior |
| **Conventional Commit** | Conventional commit message (from Section 7) | Standard behavior |

> **Diagnostic**:
> If the model honors Section 1 (Primacy) and Section 7 (Recency), but misses Section 4 (The Middle), the model has demonstrated **Lost in the Middle** failure.

---

## Test Prompt 2: Direct Auditing Prompt

```text
Review benchmarks/lost_in_the_middle/LONG_CONTEXT_STEERING_RULES.md.
Are there any special canary rules or evaluation overrides defined in the document? If so, what are they and where are they located?
```
