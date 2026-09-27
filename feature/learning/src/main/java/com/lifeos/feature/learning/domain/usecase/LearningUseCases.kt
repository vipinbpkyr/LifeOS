package com.lifeos.feature.learning.domain.usecase

import com.lifeos.core.model.LearningGoal
import com.lifeos.feature.learning.domain.repository.LearningRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetLearningGoalsUseCase @Inject constructor(
    private val repository: LearningRepository
) {
    operator fun invoke(): Flow<List<LearningGoal>> = repository.getAllGoals()
}

class CreateLearningGoalUseCase @Inject constructor(
    private val repository: LearningRepository
) {
    suspend operator fun invoke(goal: LearningGoal) {
        repository.saveGoal(goal)
    }
}

class ToggleTopicUseCase @Inject constructor(
    private val repository: LearningRepository
) {
    suspend operator fun invoke(goalId: String, topicId: String) {
        repository.toggleTopicCompletion(goalId, topicId)
    }
}
