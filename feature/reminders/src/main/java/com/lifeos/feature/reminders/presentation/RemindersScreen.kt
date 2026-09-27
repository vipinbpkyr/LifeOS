package com.lifeos.feature.reminders.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
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
        onAddReminder = { title, priority, category ->
            viewModel.addReminder(title, priority, category)
        },
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
    onAddReminder: (String, ReminderPriority, String) -> Unit,
    onToggleCompletion: (String) -> Unit,
    onDeleteReminder: (String) -> Unit,
    onToggleFilter: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var showCreateDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true },
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

            // Quick add textfield with direct submit button and keyboard IME action
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = uiState.newReminderTitle,
                    onValueChange = onTitleChanged,
                    placeholder = { Text("What needs to get done?") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (uiState.newReminderTitle.isNotBlank()) {
                                onAddReminder(uiState.newReminderTitle, ReminderPriority.MEDIUM, "Personal")
                            }
                        }
                    ),
                    trailingIcon = {
                        if (uiState.newReminderTitle.isNotBlank()) {
                            IconButton(
                                onClick = {
                                    onAddReminder(uiState.newReminderTitle, ReminderPriority.MEDIUM, "Personal")
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Send,
                                    contentDescription = "Submit Task",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        onAddReminder(uiState.newReminderTitle, ReminderPriority.MEDIUM, "Personal")
                    },
                    enabled = uiState.newReminderTitle.isNotBlank()
                ) {
                    Text("Add")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

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
                        text = "No reminders scheduled. Type above and tap 'Add', or tap '+' to create one!",
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

    if (showCreateDialog) {
        CreateReminderDialog(
            onDismiss = { showCreateDialog = false },
            onConfirm = { title, priority, category ->
                onAddReminder(title, priority, category)
                showCreateDialog = false
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CreateReminderDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, ReminderPriority, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(ReminderPriority.MEDIUM) }
    var category by remember { mutableStateOf("Personal") }

    val categories = listOf("Personal", "Work", "Finance", "Health", "Learning")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create New Task") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task description") },
                    placeholder = { Text("e.g. Schedule team retro") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Text(text = "Priority", style = MaterialTheme.typography.labelMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    ReminderPriority.entries.forEach { p ->
                        FilterChip(
                            selected = priority == p,
                            onClick = { priority = p },
                            label = { Text(p.name.lowercase().replaceFirstChar { it.uppercase() }) }
                        )
                    }
                }

                Text(text = "Category", style = MaterialTheme.typography.labelMedium)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.forEach { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onConfirm(title, priority, category)
                    }
                },
                enabled = title.isNotBlank()
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
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
            onAddReminder = { _, _, _ -> },
            onToggleCompletion = {},
            onDeleteReminder = {},
            onToggleFilter = {}
        )
    }
}
