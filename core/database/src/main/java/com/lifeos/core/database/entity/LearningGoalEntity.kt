package com.lifeos.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lifeos.core.model.LearningGoal
import com.lifeos.core.model.LearningTopic

@Entity(tableName = "learning_goals")
data class LearningGoalEntity(
    @PrimaryKey val id: String,
    val title: String,
    val category: String,
    val targetCompletionDays: Int,
    val progressPercentage: Int,
    val topics: List<LearningTopic>,
    val aiGeneratedSyllabus: String?,
    val createdAt: Long
) {
    fun toDomainModel(): LearningGoal = LearningGoal(
        id = id,
        title = title,
        category = category,
        targetCompletionDays = targetCompletionDays,
        progressPercentage = progressPercentage,
        topics = topics,
        aiGeneratedSyllabus = aiGeneratedSyllabus,
        createdAt = createdAt
    )

    companion object {
        fun fromDomainModel(goal: LearningGoal): LearningGoalEntity = LearningGoalEntity(
            id = goal.id,
            title = goal.title,
            category = goal.category,
            targetCompletionDays = goal.targetCompletionDays,
            progressPercentage = goal.progressPercentage,
            topics = goal.topics,
            aiGeneratedSyllabus = goal.aiGeneratedSyllabus,
            createdAt = goal.createdAt
        )
    }
}
