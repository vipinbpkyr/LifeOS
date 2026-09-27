package com.lifeos.feature.learning.data.repository

import com.lifeos.core.database.dao.LearningDao
import com.lifeos.core.database.entity.LearningGoalEntity
import com.lifeos.core.model.LearningGoal
import com.lifeos.feature.learning.domain.repository.LearningRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LearningRepositoryImpl @Inject constructor(
    private val learningDao: LearningDao
) : LearningRepository {

    override fun getAllGoals(): Flow<List<LearningGoal>> =
        learningDao.getAllGoals().map { list -> list.map { it.toDomainModel() } }

    override suspend fun getGoalById(id: String): LearningGoal? =
        learningDao.getGoalById(id)?.toDomainModel()

    override suspend fun saveGoal(goal: LearningGoal) {
        learningDao.insertGoal(LearningGoalEntity.fromDomainModel(goal))
    }

    override suspend fun toggleTopicCompletion(goalId: String, topicId: String) {
        val existing = learningDao.getGoalById(goalId) ?: return
        val domain = existing.toDomainModel()
        val updatedTopics = domain.topics.map { topic ->
            if (topic.id == topicId) topic.copy(isCompleted = !topic.isCompleted) else topic
        }
        val completedCount = updatedTopics.count { it.isCompleted }
        val percentage = if (updatedTopics.isNotEmpty()) (completedCount * 100) / updatedTopics.size else 0

        val updatedGoal = domain.copy(
            topics = updatedTopics,
            progressPercentage = percentage
        )
        learningDao.updateGoal(LearningGoalEntity.fromDomainModel(updatedGoal))
    }
}
