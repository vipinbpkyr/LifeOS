package com.lifeos.feature.travel.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifeos.core.model.ItineraryActivity
import com.lifeos.core.model.ItineraryDay
import com.lifeos.core.model.PackingItem
import com.lifeos.core.model.TravelPlan
import com.lifeos.feature.travel.domain.usecase.CreateTravelPlanUseCase
import com.lifeos.feature.travel.domain.usecase.GetTravelPlansUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class TravelViewModel @Inject constructor(
    getTravelPlansUseCase: GetTravelPlansUseCase,
    private val createTravelPlanUseCase: CreateTravelPlanUseCase
) : ViewModel() {

    private val destinationInput = MutableStateFlow("")
    private val budgetInput = MutableStateFlow("")

    val uiState: StateFlow<TravelUiState> = combine(
        getTravelPlansUseCase(),
        destinationInput,
        budgetInput
    ) { plans, destination, budget ->
        TravelUiState(
            plans = plans,
            nextTrip = plans.firstOrNull(),
            destinationInput = destination,
            budgetInput = budget,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TravelUiState(isLoading = true)
    )

    fun onDestinationChanged(value: String) { destinationInput.value = value }
    fun onBudgetChanged(value: String) { budgetInput.value = value }

    fun addTravelPlan() {
        val dest = destinationInput.value.trim()
        val budget = budgetInput.value.toDoubleOrNull() ?: 1200.0
        if (dest.isBlank()) return

        viewModelScope.launch {
            val plan = TravelPlan(
                id = UUID.randomUUID().toString(),
                destination = dest,
                startDate = System.currentTimeMillis() + 864000000L,
                endDate = System.currentTimeMillis() + 1209600000L,
                estimatedBudget = budget,
                days = listOf(
                    ItineraryDay(
                        dayNumber = 1,
                        dateString = "Day 1",
                        theme = "Exploring Central $dest",
                        activities = listOf(
                            ItineraryActivity("10:00 AM", "City Tour", "Sightseeing & Architecture")
                        )
                    )
                ),
                packingList = listOf(
                    PackingItem(UUID.randomUUID().toString(), "Travel Adapter"),
                    PackingItem(UUID.randomUUID().toString(), "Documents")
                )
            )
            createTravelPlanUseCase(plan)
            destinationInput.value = ""
            budgetInput.value = ""
        }
    }
}
