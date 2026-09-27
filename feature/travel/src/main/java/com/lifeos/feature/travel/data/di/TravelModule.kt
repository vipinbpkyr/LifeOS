package com.lifeos.feature.travel.data.di

import com.lifeos.core.ai.AiTool
import com.lifeos.feature.travel.data.ai.GenerateTravelPlanAiTool
import com.lifeos.feature.travel.data.repository.TravelRepositoryImpl
import com.lifeos.feature.travel.domain.repository.TravelRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TravelModule {

    @Binds
    @Singleton
    abstract fun bindTravelRepository(
        impl: TravelRepositoryImpl
    ): TravelRepository

    @Binds
    @IntoSet
    abstract fun bindGenerateTravelPlanAiTool(
        tool: GenerateTravelPlanAiTool
    ): AiTool
}
