package com.lifeos.feature.reminders.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lifeos.core.designsystem.component.AiBadge
import com.lifeos.core.designsystem.component.LifeOsCard
import com.lifeos.core.designsystem.theme.LifeOsTheme
import com.lifeos.core.model.Reminder
import com.lifeos.core.model.ReminderPriority

@Composable
fun RemindersRoute(
    viewModel: RemindersViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RemindersScreen(
        uiState = uiState,
        onTitleChanged = viewModel::onTitleChanged,
        onAddReminder = { viewModel.addReminder() },
        onToggleCompletion = viewModel::toggleCompletion,
        onDeleteReminder = viewModel::deleteReminder,
        onToggleFilter = viewModel::onToggleFilter,
        modifier = modifier
    )
}

@Composable
fun RemindersScreen(
    uiState: RemindersUiState,
    onTitleChanged: (String) -> Unit,
    onAddReminder: () -> Unit,
    onToggleCompletion: (String) -> Unit,
    onDeleteReminder: (String) -> Unit,
    onToggleFilter: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddReminder,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Reminder")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Smart Reminders",
                    style = MaterialTheme.typography.headlineMedium
                )
                AiBadge(text = "Smart Sync")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Quick add textfield
            OutlinedTextField(
                value = uiState.newReminderTitle,
                onValueChange = onTitleChanged,
                placeholder = { Text("What needs to get done? (e.g. Call broker)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Filter chips
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = !uiState.filterOnlyPending,
                    onClick = { onToggleFilter(false) },
                    label = { Text("All (${uiState.reminders.size})") }
                )
                FilterChip(
                    selected = uiState.filterOnlyPending,
                    onClick = { onToggleFilter(true) },
                    label = { Text("Active") }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (uiState.reminders.isEmpty()) {
                LifeOsCard {
                    Text(
                        text = "No reminders scheduled. Ask AI Copilot or tap '+' above to add one!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.reminders, key = { it.id }) { reminder ->
                        ReminderRow(
                            reminder = reminder,
                            onToggle = { onToggleCompletion(reminder.id) },
                            onDelete = { onDeleteReminder(reminder.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReminderRow(
    reminder: Reminder,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    LifeOsCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = reminder.isCompleted,
                onCheckedChange = { onToggle() }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = reminder.title,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (reminder.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )
                if (reminder.aiSuggestedCategory != null) {
                    Text(
                        text = "Tag: ${reminder.aiSuggestedCategory} • ${reminder.priority}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RemindersScreenPreview() {
    LifeOsTheme {
        RemindersScreen(
            uiState = RemindersUiState(
                reminders = listOf(
                    Reminder(
                        id = "1",
                        title = "Review Q3 trading performance",
                        dueTimestamp = 1700000000000L,
                        isCompleted = false,
                        priority = ReminderPriority.HIGH,
                        aiSuggestedCategory = "Finance"
                    ),
                    Reminder(
                        id = "2",
                        title = "Pack passport and travel adapter",
                        dueTimestamp = 1700000000000L,
                        isCompleted = true,
                        priority = ReminderPriority.MEDIUM,
                        aiSuggestedCategory = "Travel"
                    )
                ),
                newReminderTitle = "",
                filterOnlyPending = false
            ),
            onTitleChanged = {},
            onAddReminder = {},
            onToggleCompletion = {},
            onDeleteReminder = {},
            onToggleFilter = {}
        )
    }
}
