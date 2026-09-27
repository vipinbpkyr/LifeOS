package com.lifeos.core.model

import kotlinx.serialization.Serializable

@Serializable
data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

@Serializable
data class LearningTopic(
    val id: String,
    val title: String,
    val isCompleted: Boolean = false,
    val notes: String = "",
    val keyTakeaways: List<String> = emptyList(),
    val aiGeneratedSummary: String? = null
)

@Serializable
data class LearningGoal(
    val id: String,
    val title: String,
    val category: String, // e.g. "Android Architecture", "Deep Learning", "Options Trading"
    val targetCompletionDays: Int,
    val progressPercentage: Int = 0,
    val topics: List<LearningTopic> = emptyList(),
    val aiGeneratedSyllabus: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
