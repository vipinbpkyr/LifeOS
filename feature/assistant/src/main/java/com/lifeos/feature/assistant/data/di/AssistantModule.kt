package com.lifeos.feature.assistant.data.di

import com.lifeos.feature.assistant.data.repository.AiAssistantRepositoryImpl
import com.lifeos.feature.assistant.domain.repository.AiAssistantRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AssistantModule {

    @Binds
    @Singleton
    abstract fun bindAiAssistantRepository(
        impl: AiAssistantRepositoryImpl
    ): AiAssistantRepository
}
