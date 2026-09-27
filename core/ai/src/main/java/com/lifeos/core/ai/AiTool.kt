package com.lifeos.core.ai

interface AiTool {
    val name: String
    val description: String
    suspend fun execute(argumentsJson: String): String
}
