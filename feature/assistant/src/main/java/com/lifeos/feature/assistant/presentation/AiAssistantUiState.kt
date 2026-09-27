package com.lifeos.feature.assistant.presentation

import com.lifeos.core.model.AiMessage

data class AiAssistantUiState(
    val messages: List<AiMessage> = emptyList(),
    val currentPrompt: String = "",
    val isThinking: Boolean = false,
    val suggestedPrompts: List<String> = listOf(
        "Remind me to review Tesla trades tomorrow",
        "How is my trading risk-to-reward ratio?",
        "Plan a 3-day trip to Tokyo with \$1500 budget",
        "Generate a 7-day learning syllabus for Kotlin"
    )
)
