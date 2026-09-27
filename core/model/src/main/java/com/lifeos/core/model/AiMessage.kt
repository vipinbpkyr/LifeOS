package com.lifeos.core.model

import kotlinx.serialization.Serializable

@Serializable
enum class AiRole {
    USER,
    ASSISTANT,
    SYSTEM,
    TOOL
}

@Serializable
data class AiToolExecution(
    val toolName: String,
    val argumentsJson: String,
    val executionResult: String? = null
)

@Serializable
data class AiMessage(
    val id: String,
    val role: AiRole,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val toolExecutions: List<AiToolExecution> = emptyList()
)

@Serializable
data class DashboardSummary(
    val pendingRemindersCount: Int,
    val upcomingUrgentReminders: List<Reminder>,
    val openTradesCount: Int,
    val totalRealizedPnL: Double,
    val activeLearningGoalsCount: Int,
    val upcomingTravelDestination: String?,
    val aiDailyBriefing: String
)
