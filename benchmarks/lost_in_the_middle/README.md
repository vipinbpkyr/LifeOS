# "Lost in the Middle" Benchmark & Steering Demonstration

This directory provides an empirical demonstration of the **"Lost in the Middle" (LITM)** phenomenon observed in Transformer-based Large Language Models (LLMs) when processing long context windows, and how it impacts AI-assisted software engineering.

---

## 1. What is the "Lost in the Middle" Problem?

Research by *Liu et al. (Stanford, UC Berkeley, Chicago)* demonstrated that LLM retrieval accuracy and reasoning performance follow a **U-shaped curve**:

```text
Performance / Retrieval Accuracy (%)
100% | **********                                    **********
     |           *                                  *
     |            *                                *
 50% |             *                              *
     |              *                            *
     |               ****************************
  0% └─────────────────────────────────────────────────────────
     0% (Top/Start)        50% (The Middle)       100% (Bottom/End)
     [Primacy Bias]       [ATTENTION VALLEY]       [Recency Bias]
```

### Why Does This Happen?
1. **Positional Encoding & Attention Attenuation**: Transformer self-attention mechanisms naturally give stronger weights to tokens near the start (system prompt / initial instructions) and tokens near the end (the immediate query and recent context).
2. **Context Dilution**: As context windows expand (4k -> 32k -> 128k+ tokens), tokens in the middle (40%–60% depth) experience a significant drop in activation strength, leading models to overlook, omit, or contradict rules buried in the middle of long files like `.cursorrules`, `AGENTS.md`, or multi-file prompts.

---

## 2. The Steering Benchmark Structure

We have constructed a realistic, comprehensive engineering document:
**[`LONG_CONTEXT_STEERING_RULES.md`](file:///c:/Drive/AndroidStudioProjects/LifeOS/benchmarks/lost_in_the_middle/LONG_CONTEXT_STEERING_RULES.md)**

It contains 7 architectural sections:
- **0% - 15% (Primacy)**: Multi-module graph rules, UDF conventions, Clean Architecture.
- **15% - 45%**: Jetpack Compose standards, Hilt dependency injection, Room database.
- **48% - 52% (THE MIDDLE VALLEY — Section 4)**:
  - **The Canary Needle (`Rule 42`)**:
    A mandatory, specific override requiring the AI to:
    1. Include `const val CANARY_TOKEN = "LITM_BURIED_RULE_FOUND_DEPTH_50"`
    2. Prefix functions with `litmVerified_`
    3. Output header `[CANARY-ACTIVE: MIDDLE_OF_PROMPT_RETRIEVED]`
- **55% - 85%**: Network error handling, Konsist rules, Detekt code analysis.
- **85% - 100% (Recency)**: Git commit conventions, Gradle verification commands.

---

## 3. How to Run the Demonstration

### Experiment Protocol:
1. Provide the AI code agent with [`LONG_CONTEXT_STEERING_RULES.md`](file:///c:/Drive/AndroidStudioProjects/LifeOS/benchmarks/lost_in_the_middle/LONG_CONTEXT_STEERING_RULES.md).
2. Ask the agent a standard feature implementation prompt (e.g. from [`TEST_PROMPTS.md`](file:///c:/Drive/AndroidStudioProjects/LifeOS/benchmarks/lost_in_the_middle/TEST_PROMPTS.md)):
   > *"Based on the project steering directives, write a new ViewModel and UseCase for habit tracking."*
3. **Inspect the Output**:
   - **FAILED (Lost in the Middle)**: The LLM generates standard Clean Architecture code matching the top and bottom rules (it uses `ViewModel`, `StateFlow`, `invoke`, Conventional Commits), but **completely misses** the Canary requirements (no `litmVerified_` prefix, no `CANARY_TOKEN`, no confirmation header).
   - **PASSED**: The LLM successfully retrieves the directive located at depth 50% and complies with the canary constraints.

---

## 4. Benchmark A/B/C Test Variants

To prove that the failure is purely positional:
- **Test A (Needle in Middle - 50% Depth)**: Use [`LONG_CONTEXT_STEERING_RULES.md`](file:///c:/Drive/AndroidStudioProjects/LifeOS/benchmarks/lost_in_the_middle/LONG_CONTEXT_STEERING_RULES.md). Most models fail or require multiple attempts.
- **Test B (Needle at Start - 0% Depth)**: Move Section 4 to the top. Notice the success rate jumps close to 100%.
- **Test C (Needle at End - 100% Depth)**: Move Section 4 to the very bottom. Notice the success rate also stays high due to recency bias.
