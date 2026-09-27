# Engineering Mitigations: How to Prevent "Lost in the Middle" in Production

When developing AI-assisted applications with coding agents, steering files (`.cursorrules`, `AGENTS.md`, `.github/copilot-instructions.md`) can easily balloon to several thousand words.

Here are the 5 industry-standard architectural steering patterns to eliminate "Lost in the Middle" degradation:

---

## 1. The "Rule Indexing & Front-Loading" Pattern
Always place a concise **Rule Manifest / Table of Contents** at the top 5% of the file:
```markdown
# AGENTS.md

## High-Priority Guardrail Manifest
1. Domain Purity: Zero android.* imports (See Section 3)
2. DI: Hilt @Binds for repositories (See Section 4)
3. Quality: Must pass ./gradlew detekt (See Section 6)
```
*Why this works:* Transformers attend to the top index tokens first, creating associative anchors that keep middle sections accessible.

---

## 2. Chunking & Hierarchical Rules (Modular Rules)
Never store all steering instructions in a single monolithic 5,000-line file.
Instead, break rules into domain-specific, scoped rule files:
```text
.antigravity/rules/
├── 00-core-architecture.md     # Top-level Clean Architecture
├── 10-compose-guidelines.md    # Loaded only during UI work
├── 20-database-rules.md        # Room DB and entity standards
└── 30-quality-gates.md         # Detekt and Konsist requirements
```
*Why this works:* Each file remains under 500-1,000 tokens, staying completely within the LLM's high-attention zones.

---

## 3. Sandwich Anchoring (Primacy + Recency Redundancy)
State critical non-negotiable constraints **twice**:
- Once in the opening summary.
- Once in the closing checklist / verification requirements.

Example:
```text
[TOP OF FILE]
CRITICAL: Never import android.* in domain packages.

... [middle implementation details] ...

[BOTTOM OF FILE]
FINAL VERIFICATION CHECKLIST:
- Did you verify 0 android.* imports in domain packages?
```

---

## 4. Automated Tool Enforcement (Konsist & Detekt as Guardrails)
Never rely solely on LLM prompt obedience for critical rules.
- LLMs are probabilistic; prompt retention can fluctuate.
- Automate rules using **Konsist** (architectural tests) and **Detekt** (static linting).
- When an agent forgets a rule buried in a prompt, the build tool or test fails deterministically, providing immediate, focused error feedback.

---

## 5. Explicit Recency Re-injection in Prompts
When prompting an AI agent to do a task, re-inject the specific constraint at the end of the query:
```text
Prompt:
"Implement the HabitRepository. REMINDER: Keep domain packages pure Kotlin with zero Android framework imports."
```
This leverages the **Recency Bias** to ensure 100% adherence.
