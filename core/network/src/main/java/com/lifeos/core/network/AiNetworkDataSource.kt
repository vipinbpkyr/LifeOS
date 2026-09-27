package com.lifeos.core.network

import com.lifeos.core.network.model.AiPromptDto
import com.lifeos.core.network.model.AiResponseDto
import com.lifeos.core.network.model.AiToolExecutionDto
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.delay

interface AiNetworkDataSource {
    suspend fun queryAi(prompt: AiPromptDto): AiResponseDto
}

@Singleton
class OfflineFirstAiNetworkDataSource @Inject constructor() : AiNetworkDataSource {

    override suspend fun queryAi(prompt: AiPromptDto): AiResponseDto {
        // Simulate network latency for realistic AI experience
        delay(600)
        val text = prompt.userPrompt.lowercase()

        return when {
            text.contains("remind") -> {
                val reminderTitle = prompt.userPrompt
                    .replace(Regex("(?i)remind me to|set a reminder for|reminder:"), "")
                    .trim()
                    .ifBlank { "Important Task" }

                AiResponseDto(
                    textReply = "I have scheduled a reminder: \"$reminderTitle\" with high priority.",
                    toolExecutions = listOf(
                        AiToolExecutionDto(
                            toolName = "create_reminder",
                            argumentsJson = """{"title":"$reminderTitle","priority":"HIGH"}"""
                        )
                    )
                )
            }

            text.contains("trade") || text.contains("trading") || text.contains("pnl") -> {
                AiResponseDto(
                    textReply = "Here is your trading psychology review: Maintain strict stop-loss discipline. Your risk-reward ratio is currently optimized above 2.0. Avoid revenge trading on high volatility days.",
                    toolExecutions = listOf(
                        AiToolExecutionDto(
                            toolName = "analyze_trading",
                            argumentsJson = """{"advice":"Maintain risk management"}"""
                        )
                    )
                )
            }

            text.contains("trip") || text.contains("travel") || text.contains("itinerary") -> {
                val destination = if (text.contains("tokyo")) "Tokyo"
                else if (text.contains("paris")) "Paris"
                else if (text.contains("bali")) "Bali"
                else "Kyoto, Japan"

                AiResponseDto(
                    textReply = "I have prepared a curated 3-day travel itinerary for $destination with estimated budget and essential packing checklist.",
                    toolExecutions = listOf(
                        AiToolExecutionDto(
                            toolName = "generate_travel_plan",
                            argumentsJson = """{"destination":"$destination","budget":1500.0}"""
                        )
                    )
                )
            }

            text.contains("learn") || text.contains("study") || text.contains("quiz") -> {
                AiResponseDto(
                    textReply = "I've structured a 7-day active learning syllabus with active-recall quizzes to boost long-term retention.",
                    toolExecutions = listOf(
                        AiToolExecutionDto(
                            toolName = "create_learning_syllabus",
                            argumentsJson = """{"topic":"Modern Architecture & AI","days":7}"""
                        )
                    )
                )
            }

            else -> {
                AiResponseDto(
                    textReply = "LifeOS Copilot at your service. I can orchestrate smart reminders, " +
                        "analyze trading journals, generate active-learning syllabi, and craft travel plans."
                )
            }
        }
    }
}
