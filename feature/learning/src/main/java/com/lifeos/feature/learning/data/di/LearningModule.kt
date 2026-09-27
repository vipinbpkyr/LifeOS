package com.lifeos.feature.learning.data.di

import com.lifeos.core.ai.AiTool
import com.lifeos.feature.learning.data.ai.CreateLearningSyllabusAiTool
import com.lifeos.feature.learning.data.repository.LearningRepositoryImpl
import com.lifeos.feature.learning.domain.repository.LearningRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LearningModule {

    @Binds
    @Singleton
    abstract fun bindLearningRepository(
        impl: LearningRepositoryImpl
    ): LearningRepository

    @Binds
    @IntoSet
    abstract fun bindCreateLearningSyllabusAiTool(
        tool: CreateLearningSyllabusAiTool
    ): AiTool
}
