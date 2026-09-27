package com.lifeos.feature.dashboard.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.lifeos.core.designsystem.theme.LifeOsSuccess
import com.lifeos.core.designsystem.theme.LifeOsTheme
import com.lifeos.core.model.DashboardSummary

@Composable
fun DashboardRoute(
    onNavigateToReminders: () -> Unit,
    onNavigateToTrading: () -> Unit,
    onNavigateToLearning: () -> Unit,
    onNavigateToTravel: () -> Unit,
    onNavigateToAssistant: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DashboardScreen(
        uiState = uiState,
        onNavigateToReminders = onNavigateToReminders,
        onNavigateToTrading = onNavigateToTrading,
        onNavigateToLearning = onNavigateToLearning,
        onNavigateToTravel = onNavigateToTravel,
        onNavigateToAssistant = onNavigateToAssistant,
        modifier = modifier
    )
}

@Composable
fun DashboardScreen(
    uiState: DashboardUiState,
    onNavigateToReminders: () -> Unit,
    onNavigateToTrading: () -> Unit,
    onNavigateToLearning: () -> Unit,
    onNavigateToTravel: () -> Unit,
    onNavigateToAssistant: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Scaffold(modifier = modifier.fillMaxSize()) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "LifeOS Hub",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = "Personal Intelligence & Life Tools",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                AiBadge(text = "Autonomous")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // AI Daily Briefing Card
            LifeOsCard(onClick = onNavigateToAssistant) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                        Text(
                            text = "AI Daily Briefing",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = uiState.summary.aiDailyBriefing,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tap to chat with LifeOS Copilot ->",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // High Level Metrics
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(
                    title = "Pending Tasks",
                    value = "${uiState.summary.pendingRemindersCount}",
                    icon = Icons.Default.EventNote,
                    accentColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Realized PnL",
                    value = "\$${String.format("%.2f", uiState.summary.totalRealizedPnL)}",
                    icon = Icons.Default.ShowChart,
                    accentColor = LifeOsSuccess,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(
                    title = "Active Goals",
                    value = "${uiState.summary.activeLearningGoalsCount}",
                    icon = Icons.Default.School,
                    accentColor = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Next Trip",
                    value = uiState.summary.upcomingTravelDestination ?: "None",
                    icon = Icons.Default.FlightTakeoff,
                    accentColor = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Life Tools Modules",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Tool Shortcuts
            LifeOsCard(onClick = onNavigateToReminders) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Reminders & Tasks", style = MaterialTheme.typography.titleMedium)
                        Text("Contextual scheduling & alerts", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text("Open", color = MaterialTheme.colorScheme.primary)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LifeOsCard(onClick = onNavigateToTrading) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Trading Journal", style = MaterialTheme.typography.titleMedium)
                        Text("Risk metrics & post-trade psychology review", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text("Open", color = MaterialTheme.colorScheme.primary)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LifeOsCard(onClick = onNavigateToLearning) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Continuous Learning", style = MaterialTheme.typography.titleMedium)
                        Text("Active recall syllabus & roadmaps", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text("Open", color = MaterialTheme.colorScheme.primary)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LifeOsCard(onClick = onNavigateToTravel) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Travel Planning", style = MaterialTheme.typography.titleMedium)
                        Text("Itineraries, packing checklists & budgets", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text("Open", color = MaterialTheme.colorScheme.primary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onNavigateToAssistant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null)
                Spacer(modifier = Modifier.padding(4.dp))
                Text("Open LifeOS AI Copilot")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DashboardScreenPreview() {
    LifeOsTheme {
        DashboardScreen(
            uiState = DashboardUiState(
                summary = DashboardSummary(
                    pendingRemindersCount = 3,
                    upcomingUrgentReminders = emptyList(),
                    openTradesCount = 2,
                    totalRealizedPnL = 1250.50,
                    activeLearningGoalsCount = 4,
                    upcomingTravelDestination = "Tokyo",
                    aiDailyBriefing = "Good morning! Focus on risk management today."
                ),
                isLoading = false
            ),
            onNavigateToReminders = {},
            onNavigateToTrading = {},
            onNavigateToLearning = {},
            onNavigateToTravel = {},
            onNavigateToAssistant = {}
        )
    }
}
