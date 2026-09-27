package com.lifeos.feature.tradingjournal.presentation

import com.lifeos.core.model.TradeEntry
import com.lifeos.feature.tradingjournal.domain.usecase.TradingStats

data class TradingUiState(
    val trades: List<TradeEntry> = emptyList(),
    val stats: TradingStats = TradingStats(0, 0, 0.0, 0.0),
    val isLoading: Boolean = false,
    val tickerInput: String = "",
    val entryPriceInput: String = "",
    val quantityInput: String = "",
    val stopLossInput: String = "",
    val takeProfitInput: String = ""
)
