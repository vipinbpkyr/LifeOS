package com.lifeos.feature.tradingjournal.domain.repository

import com.lifeos.core.model.TradeEntry
import kotlinx.coroutines.flow.Flow

interface TradingRepository {
    fun getAllTrades(): Flow<List<TradeEntry>>
    fun getOpenTrades(): Flow<List<TradeEntry>>
    suspend fun getTradeById(id: String): TradeEntry?
    suspend fun logTrade(trade: TradeEntry)
    suspend fun updateTrade(trade: TradeEntry)
}
