package com.lifeos.feature.travel.domain.usecase

import com.lifeos.core.model.TravelPlan
import com.lifeos.feature.travel.domain.repository.TravelRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetTravelPlansUseCase @Inject constructor(
    private val repository: TravelRepository
) {
    operator fun invoke(): Flow<List<TravelPlan>> = repository.getAllPlans()
}

class CreateTravelPlanUseCase @Inject constructor(
    private val repository: TravelRepository
) {
    suspend operator fun invoke(plan: TravelPlan) {
        repository.savePlan(plan)
    }
}
