package com.lifeos.feature.dashboard.domain.usecase

import com.lifeos.core.model.DashboardSummary
import com.lifeos.feature.dashboard.domain.repository.DashboardRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetDashboardSummaryUseCase @Inject constructor(
    private val repository: DashboardRepository
) {
    operator fun invoke(): Flow<DashboardSummary> = repository.getDashboardSummary()
}
