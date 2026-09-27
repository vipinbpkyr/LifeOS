package com.lifeos.feature.dashboard.presentation

import com.lifeos.core.model.DashboardSummary

data class DashboardUiState(
    val summary: DashboardSummary = DashboardSummary(
        pendingRemindersCount = 0,
        upcomingUrgentReminders = emptyList(),
        openTradesCount = 0,
        totalRealizedPnL = 0.0,
        activeLearningGoalsCount = 0,
        upcomingTravelDestination = null,
        aiDailyBriefing = "Initializing your daily briefing..."
    ),
    val isLoading: Boolean = false
)
