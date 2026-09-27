package com.lifeos.feature.tradingjournal.data.repository

import com.lifeos.core.database.dao.TradeDao
import com.lifeos.core.database.entity.TradeEntity
import com.lifeos.core.model.TradeEntry
import com.lifeos.feature.tradingjournal.domain.repository.TradingRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TradingRepositoryImpl @Inject constructor(
    private val tradeDao: TradeDao
) : TradingRepository {

    override fun getAllTrades(): Flow<List<TradeEntry>> =
        tradeDao.getAllTrades().map { list -> list.map { it.toDomainModel() } }

    override fun getOpenTrades(): Flow<List<TradeEntry>> =
        tradeDao.getOpenTrades().map { list -> list.map { it.toDomainModel() } }

    override suspend fun getTradeById(id: String): TradeEntry? =
        tradeDao.getTradeById(id)?.toDomainModel()

    override suspend fun logTrade(trade: TradeEntry) {
        tradeDao.insertTrade(TradeEntity.fromDomainModel(trade))
    }

    override suspend fun updateTrade(trade: TradeEntry) {
        tradeDao.updateTrade(TradeEntity.fromDomainModel(trade))
    }
}
