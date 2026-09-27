package com.lifeos.feature.travel.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lifeos.core.designsystem.component.AiBadge
import com.lifeos.core.designsystem.component.LifeOsCard
import com.lifeos.core.designsystem.component.MetricCard
import com.lifeos.core.designsystem.theme.LifeOsTheme
import com.lifeos.core.model.PackingItem
import com.lifeos.core.model.TravelDay
import com.lifeos.core.model.TravelPlan

@Composable
fun TravelRoute(
    viewModel: TravelViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TravelScreen(
        uiState = uiState,
        onDestinationChanged = viewModel::onDestinationChanged,
        onBudgetChanged = viewModel::onBudgetChanged,
        onAddTravelPlan = viewModel::addTravelPlan,
        modifier = modifier
    )
}

@Composable
fun TravelScreen(
    uiState: TravelUiState,
    onDestinationChanged: (String) -> Unit,
    onBudgetChanged: (String) -> Unit,
    onAddTravelPlan: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = modifier.fillMaxSize()) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Travel Planner",
                    style = MaterialTheme.typography.headlineMedium
                )
                AiBadge(text = "Smart Itinerary")
            }

            Spacer(modifier = Modifier.height(16.dp))

            MetricCard(
                title = "Next Adventure",
                value = uiState.nextTrip?.destination ?: "None Scheduled",
                icon = Icons.Default.FlightTakeoff,
                accentColor = MaterialTheme.colorScheme.secondary,
                subtitle = if (uiState.nextTrip != null) "Budget: \$${uiState.nextTrip.estimatedBudget}" else null
            )

            Spacer(modifier = Modifier.height(16.dp))

            LifeOsCard {
                Column {
                    Text(
                        text = "Plan New Trip",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = uiState.destinationInput,
                        onValueChange = onDestinationChanged,
                        placeholder = { Text("Destination (e.g. Zurich, Switzerland)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = uiState.budgetInput,
                        onValueChange = onBudgetChanged,
                        placeholder = { Text("Estimated Budget ($)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = onAddTravelPlan,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Create Trip")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Saved Itineraries",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (uiState.plans.isEmpty()) {
                LifeOsCard {
                    Text(
                        text = "No trips planned yet. Ask AI Copilot: \"Plan a 3-day trip to Tokyo!\"",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(uiState.plans, key = { it.id }) { plan ->
                        TravelPlanCard(plan = plan)
                    }
                }
            }
        }
    }
}

@Composable
private fun TravelPlanCard(plan: TravelPlan) {
    LifeOsCard {
        Column {
            Text(
                text = plan.destination,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Budget: \$${plan.estimatedBudget} • ${plan.days.size} Days planned",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (plan.packingList.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Packing Items: ${plan.packingList.joinToString { it.name }}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TravelScreenPreview() {
    LifeOsTheme {
        TravelScreen(
            uiState = TravelUiState(
                plans = listOf(
                    TravelPlan(
                        id = "1",
                        destination = "Tokyo, Japan",
                        startDate = "2026-11-01",
                        endDate = "2026-11-10",
                        estimatedBudget = 3500.0,
                        days = listOf(
                            TravelDay(dayNumber = 1, activities = listOf("Arrive at Haneda", "Shinjuku night walk"))
                        ),
                        packingList = listOf(
                            PackingItem(id = "p1", name = "Universal Power Adapter", isPacked = true)
                        )
                    )
                ),
                destinationInput = "Tokyo",
                budgetInput = "3500"
            ),
            onDestinationChanged = {},
            onBudgetChanged = {},
            onAddTravelPlan = {}
        )
    }
}
