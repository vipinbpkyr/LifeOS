package com.lifeos.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lifeos.core.model.ItineraryDay
import com.lifeos.core.model.PackingItem
import com.lifeos.core.model.TravelPlan

@Entity(tableName = "travel_plans")
data class TravelPlanEntity(
    @PrimaryKey val id: String,
    val destination: String,
    val startDate: Long,
    val endDate: Long,
    val estimatedBudget: Double,
    val days: List<ItineraryDay>,
    val packingList: List<PackingItem>,
    val aiTravelTips: List<String>,
    val createdAt: Long
) {
    fun toDomainModel(): TravelPlan = TravelPlan(
        id = id,
        destination = destination,
        startDate = startDate,
        endDate = endDate,
        estimatedBudget = estimatedBudget,
        days = days,
        packingList = packingList,
        aiTravelTips = aiTravelTips,
        createdAt = createdAt
    )

    companion object {
        fun fromDomainModel(plan: TravelPlan): TravelPlanEntity = TravelPlanEntity(
            id = plan.id,
            destination = plan.destination,
            startDate = plan.startDate,
            endDate = plan.endDate,
            estimatedBudget = plan.estimatedBudget,
            days = plan.days,
            packingList = plan.packingList,
            aiTravelTips = plan.aiTravelTips,
            createdAt = plan.createdAt
        )
    }
}
