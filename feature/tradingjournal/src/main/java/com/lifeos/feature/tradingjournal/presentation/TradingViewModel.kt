package com.lifeos.feature.tradingjournal.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifeos.core.model.TradeEmotion
import com.lifeos.core.model.TradeEntry
import com.lifeos.core.model.TradeStatus
import com.lifeos.core.model.TradeType
import com.lifeos.feature.tradingjournal.domain.usecase.GetTradesUseCase
import com.lifeos.feature.tradingjournal.domain.usecase.GetTradingStatsUseCase
import com.lifeos.feature.tradingjournal.domain.usecase.LogTradeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class TradingViewModel @Inject constructor(
    getTradesUseCase: GetTradesUseCase,
    getTradingStatsUseCase: GetTradingStatsUseCase,
    private val logTradeUseCase: LogTradeUseCase
) : ViewModel() {

    private val tickerInput = MutableStateFlow("")
    private val entryPriceInput = MutableStateFlow("")
    private val quantityInput = MutableStateFlow("")
    private val stopLossInput = MutableStateFlow("")
    private val takeProfitInput = MutableStateFlow("")

    val uiState: StateFlow<TradingUiState> = combine(
        getTradesUseCase(),
        getTradingStatsUseCase(),
        tickerInput,
        entryPriceInput
    ) { trades, stats, ticker, entryPrice ->
        TradingUiState(
            trades = trades,
            stats = stats,
            tickerInput = ticker,
            entryPriceInput = entryPrice,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TradingUiState(isLoading = true)
    )

    fun onTickerChanged(value: String) { tickerInput.value = value }
    fun onEntryPriceChanged(value: String) { entryPriceInput.value = value }
    fun onQuantityChanged(value: String) { quantityInput.value = value }
    fun onStopLossChanged(value: String) { stopLossInput.value = value }
    fun onTakeProfitChanged(value: String) { takeProfitInput.value = value }

    fun logNewTrade() {
        val ticker = tickerInput.value.trim().uppercase()
        val entry = entryPriceInput.value.toDoubleOrNull() ?: 100.0
        val qty = quantityInput.value.toDoubleOrNull() ?: 10.0
        val sl = stopLossInput.value.toDoubleOrNull() ?: (entry * 0.95)
        val tp = takeProfitInput.value.toDoubleOrNull() ?: (entry * 1.15)

        if (ticker.isBlank()) return

        viewModelScope.launch {
            val trade = TradeEntry(
                id = UUID.randomUUID().toString(),
                tickerSymbol = ticker,
                tradeType = TradeType.BUY_LONG,
                entryPrice = entry,
                quantity = qty,
                stopLoss = sl,
                takeProfit = tp,
                entryTimestamp = System.currentTimeMillis(),
                status = TradeStatus.OPEN,
                strategyTag = "AI Momentum",
                emotion = TradeEmotion.CONFIDENT,
                notes = "Identified via AI volatility screening."
            )
            logTradeUseCase(trade)
            tickerInput.value = ""
            entryPriceInput.value = ""
        }
    }
}
