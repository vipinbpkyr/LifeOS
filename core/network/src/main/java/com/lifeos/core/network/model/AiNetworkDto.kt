package com.lifeos.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class AiPromptDto(
    val userPrompt: String,
    val contextJson: String? = null,
    val availableTools: List<String> = emptyList()
)

@Serializable
data class AiToolExecutionDto(
    val toolName: String,
    val argumentsJson: String
)

@Serializable
data class AiResponseDto(
    val textReply: String,
    val toolExecutions: List<AiToolExecutionDto> = emptyList(),
    val confidence: Double = 0.95
)
