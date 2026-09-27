package com.lifeos.feature.reminders.domain.usecase

import com.lifeos.core.model.Reminder
import com.lifeos.feature.reminders.domain.repository.ReminderRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetRemindersUseCase @Inject constructor(
    private val repository: ReminderRepository
) {
    operator fun invoke(): Flow<List<Reminder>> = repository.getAllReminders()
}

class SaveReminderUseCase @Inject constructor(
    private val repository: ReminderRepository
) {
    suspend operator fun invoke(reminder: Reminder) {
        repository.saveReminder(reminder)
    }
}

class ToggleReminderUseCase @Inject constructor(
    private val repository: ReminderRepository
) {
    suspend operator fun invoke(id: String) {
        repository.toggleReminderCompletion(id)
    }
}

class DeleteReminderUseCase @Inject constructor(
    private val repository: ReminderRepository
) {
    suspend operator fun invoke(id: String) {
        repository.deleteReminder(id)
    }
}

class GetReminderByIdUseCase @Inject constructor(
    private val repository: ReminderRepository
) {
    operator fun invoke(id: String): Flow<Reminder?> = repository.getReminderStream(id)
}

class UpdateReminderUseCase @Inject constructor(
    private val repository: ReminderRepository
) {
    suspend operator fun invoke(reminder: Reminder) {
        repository.saveReminder(reminder)
    }
}
