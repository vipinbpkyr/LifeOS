package com.lifeos.feature.assistant.domain.repository

import com.lifeos.core.model.AiMessage
import kotlinx.coroutines.flow.Flow

interface AiAssistantRepository {
    fun getMessages(): Flow<List<AiMessage>>
    suspend fun sendPrompt(prompt: String): AiMessage
    suspend fun clearChat()
}
