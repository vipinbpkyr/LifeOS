package com.lifeos.core.model

import kotlinx.serialization.Serializable

@Serializable
enum class TradeType {
    BUY_LONG,
    SELL_SHORT
}

@Serializable
enum class TradeStatus {
    OPEN,
    CLOSED,
    CANCELLED
}

@Serializable
enum class TradeEmotion {
    CONFIDENT,
    CALM,
    FOMO,
    ANXIOUS,
    GREEDY,
    REVENGE_TRADING
}

@Serializable
data class TradeEntry(
    val id: String,
    val tickerSymbol: String,
    val tradeType: TradeType,
    val entryPrice: Double,
    val exitPrice: Double? = null,
    val quantity: Double,
    val stopLoss: Double,
    val takeProfit: Double,
    val entryTimestamp: Long,
    val exitTimestamp: Long? = null,
    val status: TradeStatus = TradeStatus.OPEN,
    val strategyTag: String = "Breakout",
    val emotion: TradeEmotion = TradeEmotion.CALM,
    val aiPostTradeReview: String? = null,
    val notes: String = ""
) {
    val realizedPnL: Double?
        get() = if (exitPrice != null) {
            when (tradeType) {
                TradeType.BUY_LONG -> (exitPrice - entryPrice) * quantity
                TradeType.SELL_SHORT -> (entryPrice - exitPrice) * quantity
            }
        } else {
            null
        }

    val riskRewardRatio: Double
        get() {
            val risk = kotlin.math.abs(entryPrice - stopLoss)
            val reward = kotlin.math.abs(takeProfit - entryPrice)
            return if (risk > 0.0) reward / risk else 0.0
        }
}
