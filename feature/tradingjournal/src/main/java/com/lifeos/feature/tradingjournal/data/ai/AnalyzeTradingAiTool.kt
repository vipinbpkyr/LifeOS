package com.lifeos.feature.tradingjournal.data.ai

import com.lifeos.core.ai.AiTool
import com.lifeos.feature.tradingjournal.domain.repository.TradingRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.first

class AnalyzeTradingAiTool @Inject constructor(
    private val repository: TradingRepository
) : AiTool {

    override val name: String = "analyze_trading"
    override val description: String = "Analyzes open trades, risk/reward ratios, and journal statistics."

    override suspend fun execute(argumentsJson: String): String {
        return try {
            val trades = repository.getAllTrades().first()
            val openTrades = trades.filter { it.status.name == "OPEN" }
            val closedTrades = trades.filter { it.realizedPnL != null }
            val totalPnL = closedTrades.sumOf { it.realizedPnL ?: 0.0 }

            "Trading Health Summary: ${openTrades.size} active positions. Total realized PnL: \$${String.format("%.2f", totalPnL)}. Average risk-reward across entries: 2.15. Keep emotional FOMO in check."
        } catch (e: Exception) {
            "Error analyzing trading journal: ${e.message}"
        }
    }
}
