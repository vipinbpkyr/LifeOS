package com.lifeos.feature.dashboard.domain.repository

import com.lifeos.core.model.DashboardSummary
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {
    fun getDashboardSummary(): Flow<DashboardSummary>
}
