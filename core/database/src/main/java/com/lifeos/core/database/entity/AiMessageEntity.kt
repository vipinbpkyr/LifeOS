package com.lifeos.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lifeos.core.model.AiMessage
import com.lifeos.core.model.AiRole

@Entity(tableName = "ai_messages")
data class AiMessageEntity(
    @PrimaryKey val id: String,
    val role: String,
    val content: String,
    val timestamp: Long
) {
    fun toDomainModel(): AiMessage = AiMessage(
        id = id,
        role = runCatching { AiRole.valueOf(role) }.getOrDefault(AiRole.ASSISTANT),
        content = content,
        timestamp = timestamp
    )

    companion object {
        fun fromDomainModel(message: AiMessage): AiMessageEntity = AiMessageEntity(
            id = message.id,
            role = message.role.name,
            content = message.content,
            timestamp = message.timestamp
        )
    }
}
