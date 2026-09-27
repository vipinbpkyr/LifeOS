package com.lifeos.feature.travel.data.ai

import com.lifeos.core.ai.AiTool
import com.lifeos.core.model.ItineraryActivity
import com.lifeos.core.model.ItineraryDay
import com.lifeos.core.model.PackingItem
import com.lifeos.core.model.TravelPlan
import com.lifeos.feature.travel.domain.repository.TravelRepository
import java.util.UUID
import javax.inject.Inject
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
private data class TravelPlanArgs(
    val destination: String,
    val budget: Double = 1500.0
)

class GenerateTravelPlanAiTool @Inject constructor(
    private val repository: TravelRepository
) : AiTool {

    override val name: String = "generate_travel_plan"
    override val description: String = "Generates a travel plan with daily itinerary, budget, and packing list."

    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun execute(argumentsJson: String): String {
        return try {
            val args = json.decodeFromString<TravelPlanArgs>(argumentsJson)
            val days = listOf(
                ItineraryDay(
                    dayNumber = 1,
                    dateString = "Day 1",
                    theme = "Arrival & City Orientation",
                    activities = listOf(
                        ItineraryActivity("10:00 AM", "Check into Hotel", "Settle in and rest", "", 0.0),
                        ItineraryActivity("02:00 PM", "Historical Landmark Tour", "Visit historic sites", "", 35.0),
                        ItineraryActivity("07:00 PM", "Authentic Local Dinner", "Sample street delicacies", "", 40.0)
                    )
                ),
                ItineraryDay(
                    dayNumber = 2,
                    dateString = "Day 2",
                    theme = "Cultural Immersion & Nature",
                    activities = listOf(
                        ItineraryActivity("09:00 AM", "Botanical Gardens", "Morning walk", "", 15.0),
                        ItineraryActivity("01:00 PM", "Art Museum", "Curated exhibit", "", 25.0)
                    )
                )
            )

            val packing = listOf(
                PackingItem(UUID.randomUUID().toString(), "Passport & Travel Documents"),
                PackingItem(UUID.randomUUID().toString(), "Power Bank & Universal Adapter"),
                PackingItem(UUID.randomUUID().toString(), "Comfortable Walking Shoes")
            )

            val plan = TravelPlan(
                id = UUID.randomUUID().toString(),
                destination = args.destination,
                startDate = System.currentTimeMillis() + 864000000L,
                endDate = System.currentTimeMillis() + 1123200000L,
                estimatedBudget = args.budget,
                days = days,
                packingList = packing,
                aiTravelTips = listOf("Book metro pass in advance", "Keep digital copies of tickets")
            )
            repository.savePlan(plan)
            "Created full itinerary for ${args.destination} with ${days.size} days planned and packing list."
        } catch (e: Exception) {
            "Failed to generate travel plan: ${e.message}"
        }
    }
}
