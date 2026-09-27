package com.lifeos.feature.travel.presentation

import com.lifeos.core.model.TravelPlan

data class TravelUiState(
    val plans: List<TravelPlan> = emptyList(),
    val nextTrip: TravelPlan? = null,
    val isLoading: Boolean = false,
    val destinationInput: String = "",
    val budgetInput: String = ""
)
