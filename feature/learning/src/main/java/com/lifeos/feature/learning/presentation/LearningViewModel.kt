package com.lifeos.feature.learning.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifeos.core.model.LearningGoal
import com.lifeos.core.model.LearningTopic
import com.lifeos.feature.learning.domain.usecase.CreateLearningGoalUseCase
import com.lifeos.feature.learning.domain.usecase.GetLearningGoalsUseCase
import com.lifeos.feature.learning.domain.usecase.ToggleTopicUseCase
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
class LearningViewModel @Inject constructor(
    getLearningGoalsUseCase: GetLearningGoalsUseCase,
    private val createLearningGoalUseCase: CreateLearningGoalUseCase,
    private val toggleTopicUseCase: ToggleTopicUseCase
) : ViewModel() {

    private val newGoalTitle = MutableStateFlow("")
    private val newGoalCategory = MutableStateFlow("Technology")

    val uiState: StateFlow<LearningUiState> = combine(
        getLearningGoalsUseCase(),
        newGoalTitle,
        newGoalCategory
    ) { goals, title, category ->
        LearningUiState(
            goals = goals,
            newGoalTitle = title,
            newGoalCategory = category,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = LearningUiState(isLoading = true)
    )

    fun onTitleChanged(title: String) { newGoalTitle.value = title }
    fun onCategoryChanged(category: String) { newGoalCategory.value = category }

    fun addGoal() {
        val title = newGoalTitle.value.trim()
        if (title.isBlank()) return

        viewModelScope.launch {
            val topics = listOf(
                LearningTopic(id = UUID.randomUUID().toString(), title = "Module 1: Principles"),
                LearningTopic(id = UUID.randomUUID().toString(), title = "Module 2: Implementation"),
                LearningTopic(id = UUID.randomUUID().toString(), title = "Module 3: Verification")
            )
            val goal = LearningGoal(
                id = UUID.randomUUID().toString(),
                title = title,
                category = newGoalCategory.value,
                targetCompletionDays = 14,
                topics = topics,
                aiGeneratedSyllabus = "Standard 3-week mastery roadmap."
            )
            createLearningGoalUseCase(goal)
            newGoalTitle.value = ""
        }
    }

    fun toggleTopic(goalId: String, topicId: String) {
        viewModelScope.launch {
            toggleTopicUseCase(goalId, topicId)
        }
    }
}
