package com.lifeos.core.ai

import com.lifeos.core.database.dao.AiMessageDao
import com.lifeos.core.database.entity.AiMessageEntity
import com.lifeos.core.model.AiMessage
import com.lifeos.core.model.AiRole
import com.lifeos.core.model.AiToolExecution
import com.lifeos.core.network.AiNetworkDataSource
import com.lifeos.core.network.model.AiPromptDto
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class AiAgentOrchestrator @Inject constructor(
    private val networkDataSource: AiNetworkDataSource,
    private val aiMessageDao: AiMessageDao,
    private val registeredTools: Set<@JvmSuppressWildcards AiTool>
) {

    fun getConversationHistory(): Flow<List<AiMessage>> =
        aiMessageDao.getAllMessages().map { entities ->
            entities.map { it.toDomainModel() }
        }

    suspend fun processUserPrompt(userPrompt: String): AiMessage {
        val userMessageId = UUID.randomUUID().toString()
        val userTimestamp = System.currentTimeMillis()

        // 1. Record user message
        aiMessageDao.insertMessage(
            AiMessageEntity(
                id = userMessageId,
                role = AiRole.USER.name,
                content = userPrompt,
                timestamp = userTimestamp
            )
        )

        // 2. Query AI engine with available tool names
        val toolNames = registeredTools.map { it.name }
        val aiResponseDto = networkDataSource.queryAi(
            AiPromptDto(
                userPrompt = userPrompt,
                availableTools = toolNames
            )
        )

        // 3. Execute tools if suggested by the AI
        val executions = mutableListOf<AiToolExecution>()
        for (toolCall in aiResponseDto.toolExecutions) {
            val matchingTool = registeredTools.find { it.name == toolCall.toolName }
            val result = if (matchingTool != null) {
                try {
                    matchingTool.execute(toolCall.argumentsJson)
                } catch (e: Exception) {
                    "Tool execution error: ${e.message}"
                }
            } else {
                "Tool '${toolCall.toolName}' is not registered."
            }
            executions.add(
                AiToolExecution(
                    toolName = toolCall.toolName,
                    argumentsJson = toolCall.argumentsJson,
                    executionResult = result
                )
            )
        }

        // 4. Save assistant response
        val assistantMessageId = UUID.randomUUID().toString()
        val assistantTimestamp = System.currentTimeMillis()
        val finalAssistantContent = aiResponseDto.textReply

        aiMessageDao.insertMessage(
            AiMessageEntity(
                id = assistantMessageId,
                role = AiRole.ASSISTANT.name,
                content = finalAssistantContent,
                timestamp = assistantTimestamp
            )
        )

        return AiMessage(
            id = assistantMessageId,
            role = AiRole.ASSISTANT,
            content = finalAssistantContent,
            timestamp = assistantTimestamp,
            toolExecutions = executions
        )
    }

    suspend fun clearHistory() {
        aiMessageDao.clearHistory()
    }
}
