package com.lifeos.feature.travel.data.repository

import com.lifeos.core.database.dao.TravelDao
import com.lifeos.core.database.entity.TravelPlanEntity
import com.lifeos.core.model.TravelPlan
import com.lifeos.feature.travel.domain.repository.TravelRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TravelRepositoryImpl @Inject constructor(
    private val travelDao: TravelDao
) : TravelRepository {

    override fun getAllPlans(): Flow<List<TravelPlan>> =
        travelDao.getAllTravelPlans().map { list -> list.map { it.toDomainModel() } }

    override fun getNextUpcomingTrip(): Flow<TravelPlan?> =
        travelDao.getNextUpcomingTrip().map { it?.toDomainModel() }

    override suspend fun getPlanById(id: String): TravelPlan? =
        travelDao.getTravelPlanById(id)?.toDomainModel()

    override suspend fun savePlan(plan: TravelPlan) {
        travelDao.insertTravelPlan(TravelPlanEntity.fromDomainModel(plan))
    }
}
