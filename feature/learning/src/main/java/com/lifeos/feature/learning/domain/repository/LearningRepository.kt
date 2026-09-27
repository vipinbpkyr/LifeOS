package com.lifeos.feature.learning.domain.repository

import com.lifeos.core.model.LearningGoal
import kotlinx.coroutines.flow.Flow

interface LearningRepository {
    fun getAllGoals(): Flow<List<LearningGoal>>
    suspend fun getGoalById(id: String): LearningGoal?
    suspend fun saveGoal(goal: LearningGoal)
    suspend fun toggleTopicCompletion(goalId: String, topicId: String)
}
