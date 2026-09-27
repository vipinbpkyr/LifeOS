package com.lifeos.feature.learning.presentation

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
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LinearProgressIndicator
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
import com.lifeos.core.designsystem.theme.LifeOsTheme
import com.lifeos.core.model.LearningGoal
import com.lifeos.core.model.LearningTopic

@Composable
fun LearningRoute(
    viewModel: LearningViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LearningScreen(
        uiState = uiState,
        onTitleChanged = viewModel::onTitleChanged,
        onAddGoal = viewModel::addGoal,
        onToggleTopic = viewModel::toggleTopic,
        modifier = modifier
    )
}

@Composable
fun LearningScreen(
    uiState: LearningUiState,
    onTitleChanged: (String) -> Unit,
    onAddGoal: () -> Unit,
    onToggleTopic: (String, String) -> Unit,
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
                    text = "Active Learning",
                    style = MaterialTheme.typography.headlineMedium
                )
                AiBadge(text = "Syllabus Generator")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Add Goal
            LifeOsCard {
                Column {
                    Text(
                        text = "Set New Learning Goal",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = uiState.newGoalTitle,
                        onValueChange = onTitleChanged,
                        placeholder = { Text("What skill are you mastering? (e.g. Jetpack Compose)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = onAddGoal,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Create Roadmap")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Learning Roadmaps",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (uiState.goals.isEmpty()) {
                LifeOsCard {
                    Text(
                        text = "No learning goals yet. Ask AI Copilot to generate a 7-day study plan!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(uiState.goals, key = { it.id }) { goal ->
                        LearningGoalCard(goal = goal, onToggleTopic = onToggleTopic)
                    }
                }
            }
        }
    }
}

@Composable
private fun LearningGoalCard(
    goal: LearningGoal,
    onToggleTopic: (String, String) -> Unit
) {
    LifeOsCard {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = goal.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${goal.progressPercentage}%",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { goal.progressPercentage / 100f },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(8.dp))
            goal.topics.forEach { topic ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = topic.isCompleted,
                        onCheckedChange = { onToggleTopic(goal.id, topic.id) }
                    )
                    Text(
                        text = topic.title,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LearningScreenPreview() {
    LifeOsTheme {
        LearningScreen(
            uiState = LearningUiState(
                goals = listOf(
                    LearningGoal(
                        id = "1",
                        title = "Jetpack Compose Internals",
                        progressPercentage = 50,
                        topics = listOf(
                            LearningTopic(id = "t1", title = "Snapshot State system", isCompleted = true),
                            LearningTopic(id = "t2", title = "Layout & SubcomposeLayout", isCompleted = false)
                        )
                    )
                ),
                newGoalTitle = ""
            ),
            onTitleChanged = {},
            onAddGoal = {},
            onToggleTopic = { _, _ -> }
        )
    }
}
