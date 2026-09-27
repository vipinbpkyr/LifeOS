package com.lifeos.feature.reminders.data.repository

import com.lifeos.core.database.dao.ReminderDao
import com.lifeos.core.database.entity.ReminderEntity
import com.lifeos.core.model.Reminder
import com.lifeos.feature.reminders.domain.repository.ReminderRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReminderRepositoryImpl @Inject constructor(
    private val reminderDao: ReminderDao
) : ReminderRepository {

    override fun getAllReminders(): Flow<List<Reminder>> =
        reminderDao.getAllReminders().map { list -> list.map { it.toDomainModel() } }

    override fun getPendingReminders(): Flow<List<Reminder>> =
        reminderDao.getPendingReminders().map { list -> list.map { it.toDomainModel() } }

    override suspend fun getReminderById(id: String): Reminder? =
        reminderDao.getReminderById(id)?.toDomainModel()

    override suspend fun saveReminder(reminder: Reminder) {
        reminderDao.insertReminder(ReminderEntity.fromDomainModel(reminder))
    }

    override suspend fun deleteReminder(id: String) {
        reminderDao.deleteReminderById(id)
    }

    override suspend fun toggleReminderCompletion(id: String) {
        val existing = reminderDao.getReminderById(id) ?: return
        reminderDao.updateReminder(existing.copy(isCompleted = !existing.isCompleted))
    }
}
