package com.lifeos.feature.learning.data.ai

import com.lifeos.core.ai.AiTool
import com.lifeos.core.model.LearningGoal
import com.lifeos.core.model.LearningTopic
import com.lifeos.feature.learning.domain.repository.LearningRepository
import java.util.UUID
import javax.inject.Inject
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
private data class CreateSyllabusArgs(
    val topic: String,
    val days: Int = 7
)

class CreateLearningSyllabusAiTool @Inject constructor(
    private val repository: LearningRepository
) : AiTool {

    override val name: String = "create_learning_syllabus"
    override val description: String = "Generates an active learning syllabus with actionable subtopics."

    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun execute(argumentsJson: String): String {
        return try {
            val args = json.decodeFromString<CreateSyllabusArgs>(argumentsJson)
            val topics = listOf(
                LearningTopic(id = UUID.randomUUID().toString(), title = "Fundamentals & Core Primitives of ${args.topic}"),
                LearningTopic(id = UUID.randomUUID().toString(), title = "Advanced Design Patterns & Architecture"),
                LearningTopic(id = UUID.randomUUID().toString(), title = "Practical Real-World Project & Quiz")
            )
            val goal = LearningGoal(
                id = UUID.randomUUID().toString(),
                title = "Mastery: ${args.topic}",
                category = "Skill Mastery",
                targetCompletionDays = args.days,
                topics = topics,
                aiGeneratedSyllabus = "AI 3-stage mastery path designed for active recall."
            )
            repository.saveGoal(goal)
            "Created learning goal for '${args.topic}' with ${topics.size} structured modules."
        } catch (e: Exception) {
            "Failed to create syllabus: ${e.message}"
        }
    }
}
