package com.lifeos.feature.tradingjournal.domain.usecase

import com.lifeos.core.model.TradeEntry
import com.lifeos.feature.tradingjournal.domain.repository.TradingRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class TradingStats(
    val totalTrades: Int,
    val openTradesCount: Int,
    val totalRealizedPnL: Double,
    val winRatePercentage: Double
)

class GetTradesUseCase @Inject constructor(
    private val repository: TradingRepository
) {
    operator fun invoke(): Flow<List<TradeEntry>> = repository.getAllTrades()
}

class LogTradeUseCase @Inject constructor(
    private val repository: TradingRepository
) {
    suspend operator fun invoke(trade: TradeEntry) {
        repository.logTrade(trade)
    }
}

class GetTradingStatsUseCase @Inject constructor(
    private val repository: TradingRepository
) {
    operator fun invoke(): Flow<TradingStats> = repository.getAllTrades().map { trades ->
        val closedTrades = trades.filter { it.realizedPnL != null }
        val winningTrades = closedTrades.filter { (it.realizedPnL ?: 0.0) > 0.0 }
        val winRate = if (closedTrades.isNotEmpty()) {
            (winningTrades.size.toDouble() / closedTrades.size.toDouble()) * 100.0
        } else {
            0.0
        }
        val totalPnL = closedTrades.sumOf { it.realizedPnL ?: 0.0 }
        val openCount = trades.count { it.status.name == "OPEN" }

        TradingStats(
            totalTrades = trades.size,
            openTradesCount = openCount,
            totalRealizedPnL = totalPnL,
            winRatePercentage = winRate
        )
    }
}
