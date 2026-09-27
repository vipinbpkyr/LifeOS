package com.lifeos.feature.assistant.domain.usecase

import com.lifeos.core.model.AiMessage
import com.lifeos.feature.assistant.domain.repository.AiAssistantRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetAiMessagesUseCase @Inject constructor(
    private val repository: AiAssistantRepository
) {
    operator fun invoke(): Flow<List<AiMessage>> = repository.getMessages()
}

class SendAiPromptUseCase @Inject constructor(
    private val repository: AiAssistantRepository
) {
    suspend operator fun invoke(prompt: String): AiMessage = repository.sendPrompt(prompt)
}

class ClearAiChatUseCase @Inject constructor(
    private val repository: AiAssistantRepository
) {
    suspend operator fun invoke() {
        repository.clearChat()
    }
}
