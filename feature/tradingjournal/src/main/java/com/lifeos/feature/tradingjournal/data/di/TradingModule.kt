package com.lifeos.feature.tradingjournal.data.di

import com.lifeos.core.ai.AiTool
import com.lifeos.feature.tradingjournal.data.ai.AnalyzeTradingAiTool
import com.lifeos.feature.tradingjournal.data.repository.TradingRepositoryImpl
import com.lifeos.feature.tradingjournal.domain.repository.TradingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TradingModule {

    @Binds
    @Singleton
    abstract fun bindTradingRepository(
        impl: TradingRepositoryImpl
    ): TradingRepository

    @Binds
    @IntoSet
    abstract fun bindAnalyzeTradingAiTool(
        tool: AnalyzeTradingAiTool
    ): AiTool
}
