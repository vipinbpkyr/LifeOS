package com.lifeos.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lifeos.core.model.TradeEmotion
import com.lifeos.core.model.TradeEntry
import com.lifeos.core.model.TradeStatus
import com.lifeos.core.model.TradeType

@Entity(tableName = "trades")
data class TradeEntity(
    @PrimaryKey val id: String,
    val tickerSymbol: String,
    val tradeType: String,
    val entryPrice: Double,
    val exitPrice: Double?,
    val quantity: Double,
    val stopLoss: Double,
    val takeProfit: Double,
    val entryTimestamp: Long,
    val exitTimestamp: Long?,
    val status: String,
    val strategyTag: String,
    val emotion: String,
    val aiPostTradeReview: String?,
    val notes: String
) {
    fun toDomainModel(): TradeEntry = TradeEntry(
        id = id,
        tickerSymbol = tickerSymbol,
        tradeType = runCatching { TradeType.valueOf(tradeType) }.getOrDefault(TradeType.BUY_LONG),
        entryPrice = entryPrice,
        exitPrice = exitPrice,
        quantity = quantity,
        stopLoss = stopLoss,
        takeProfit = takeProfit,
        entryTimestamp = entryTimestamp,
        exitTimestamp = exitTimestamp,
        status = runCatching { TradeStatus.valueOf(status) }.getOrDefault(TradeStatus.OPEN),
        strategyTag = strategyTag,
        emotion = runCatching { TradeEmotion.valueOf(emotion) }.getOrDefault(TradeEmotion.CALM),
        aiPostTradeReview = aiPostTradeReview,
        notes = notes
    )

    companion object {
        fun fromDomainModel(trade: TradeEntry): TradeEntity = TradeEntity(
            id = trade.id,
            tickerSymbol = trade.tickerSymbol,
            tradeType = trade.tradeType.name,
            entryPrice = trade.entryPrice,
            exitPrice = trade.exitPrice,
            quantity = trade.quantity,
            stopLoss = trade.stopLoss,
            takeProfit = trade.takeProfit,
            entryTimestamp = trade.entryTimestamp,
            exitTimestamp = trade.exitTimestamp,
            status = trade.status.name,
            strategyTag = trade.strategyTag,
            emotion = trade.emotion.name,
            aiPostTradeReview = trade.aiPostTradeReview,
            notes = trade.notes
        )
    }
}
