package com.lifeos.feature.learning.presentation

import com.lifeos.core.model.LearningGoal

data class LearningUiState(
    val goals: List<LearningGoal> = emptyList(),
    val isLoading: Boolean = false,
    val newGoalTitle: String = "",
    val newGoalCategory: String = "Technology"
)
