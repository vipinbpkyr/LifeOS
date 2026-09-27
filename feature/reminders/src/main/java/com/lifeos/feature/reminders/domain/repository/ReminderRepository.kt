package com.lifeos.feature.reminders.domain.repository

import com.lifeos.core.model.Reminder
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    fun getAllReminders(): Flow<List<Reminder>>
    fun getPendingReminders(): Flow<List<Reminder>>
    suspend fun getReminderById(id: String): Reminder?
    suspend fun saveReminder(reminder: Reminder)
    suspend fun deleteReminder(id: String)
    suspend fun toggleReminderCompletion(id: String)
}
