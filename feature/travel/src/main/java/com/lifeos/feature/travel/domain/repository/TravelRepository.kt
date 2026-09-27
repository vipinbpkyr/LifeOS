package com.lifeos.feature.travel.domain.repository

import com.lifeos.core.model.TravelPlan
import kotlinx.coroutines.flow.Flow

interface TravelRepository {
    fun getAllPlans(): Flow<List<TravelPlan>>
    fun getNextUpcomingTrip(): Flow<TravelPlan?>
    suspend fun getPlanById(id: String): TravelPlan?
    suspend fun savePlan(plan: TravelPlan)
}
