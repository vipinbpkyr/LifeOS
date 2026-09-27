package com.lifeos.feature.reminders.data.di

import com.lifeos.core.ai.AiTool
import com.lifeos.feature.reminders.data.ai.CreateReminderAiTool
import com.lifeos.feature.reminders.data.repository.ReminderRepositoryImpl
import com.lifeos.feature.reminders.domain.repository.ReminderRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemindersModule {

    @Binds
    @Singleton
    abstract fun bindReminderRepository(
        impl: ReminderRepositoryImpl
    ): ReminderRepository

    @Binds
    @IntoSet
    abstract fun bindCreateReminderAiTool(
        tool: CreateReminderAiTool
    ): AiTool
}
