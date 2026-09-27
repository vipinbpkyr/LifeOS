package com.lifeos.feature.assistant.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifeos.feature.assistant.domain.usecase.ClearAiChatUseCase
import com.lifeos.feature.assistant.domain.usecase.GetAiMessagesUseCase
import com.lifeos.feature.assistant.domain.usecase.SendAiPromptUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class AiAssistantViewModel @Inject constructor(
    getAiMessagesUseCase: GetAiMessagesUseCase,
    private val sendAiPromptUseCase: SendAiPromptUseCase,
    private val clearAiChatUseCase: ClearAiChatUseCase
) : ViewModel() {

    private val currentPrompt = MutableStateFlow("")
    private val isThinking = MutableStateFlow(false)

    val uiState: StateFlow<AiAssistantUiState> = combine(
        getAiMessagesUseCase(),
        currentPrompt,
        isThinking
    ) { messages, prompt, thinking ->
        AiAssistantUiState(
            messages = messages,
            currentPrompt = prompt,
            isThinking = thinking
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AiAssistantUiState()
    )

    fun onPromptChanged(prompt: String) {
        currentPrompt.value = prompt
    }

    fun submitPrompt(promptOverride: String? = null) {
        val query = promptOverride ?: currentPrompt.value.trim()
        if (query.isBlank() || isThinking.value) return

        currentPrompt.value = ""
        isThinking.value = true

        viewModelScope.launch {
            try {
                sendAiPromptUseCase(query)
            } finally {
                isThinking.value = false
            }
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            clearAiChatUseCase()
        }
    }
}
