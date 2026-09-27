package com.lifeos.core.ai.di

import com.lifeos.core.ai.AiTool
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.Multibinds

@Module
@InstallIn(SingletonComponent::class)
abstract class AiModule {

    @Multibinds
    abstract fun bindAiTools(): Set<AiTool>
}
