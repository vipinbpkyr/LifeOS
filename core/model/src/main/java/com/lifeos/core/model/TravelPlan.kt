package com.lifeos.core.model

import kotlinx.serialization.Serializable

@Serializable
data class PackingItem(
    val id: String,
    val name: String,
    val isPacked: Boolean = false,
    val category: String = "Essentials"
)

@Serializable
data class ItineraryActivity(
    val timeSlot: String,
    val title: String,
    val description: String,
    val location: String = "",
    val estimatedCost: Double = 0.0
)

@Serializable
data class ItineraryDay(
    val dayNumber: Int,
    val dateString: String,
    val theme: String,
    val activities: List<ItineraryActivity> = emptyList()
)

@Serializable
data class TravelPlan(
    val id: String,
    val destination: String,
    val startDate: Long,
    val endDate: Long,
    val estimatedBudget: Double,
    val days: List<ItineraryDay> = emptyList(),
    val packingList: List<PackingItem> = emptyList(),
    val aiTravelTips: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)
