package com.lifeos.feature.assistant.data.repository

import com.lifeos.core.ai.AiAgentOrchestrator
import com.lifeos.core.model.AiMessage
import com.lifeos.feature.assistant.domain.repository.AiAssistantRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class AiAssistantRepositoryImpl @Inject constructor(
    private val orchestrator: AiAgentOrchestrator
) : AiAssistantRepository {

    override fun getMessages(): Flow<List<AiMessage>> =
        orchestrator.getConversationHistory()

    override suspend fun sendPrompt(prompt: String): AiMessage =
        orchestrator.processUserPrompt(prompt)

    override suspend fun clearChat() {
        orchestrator.clearHistory()
    }
}
