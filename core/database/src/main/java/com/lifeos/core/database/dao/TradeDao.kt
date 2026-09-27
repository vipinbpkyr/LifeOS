package com.lifeos.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lifeos.core.database.entity.TradeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TradeDao {
    @Query("SELECT * FROM trades ORDER BY entryTimestamp DESC")
    fun getAllTrades(): Flow<List<TradeEntity>>

    @Query("SELECT * FROM trades WHERE status = 'OPEN' ORDER BY entryTimestamp DESC")
    fun getOpenTrades(): Flow<List<TradeEntity>>

    @Query("SELECT * FROM trades WHERE id = :id")
    suspend fun getTradeById(id: String): TradeEntity?

    @Query("SELECT COUNT(*) FROM trades WHERE status = 'OPEN'")
    fun getOpenTradesCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrade(trade: TradeEntity)

    @Update
    suspend fun updateTrade(trade: TradeEntity)

    @Delete
    suspend fun deleteTrade(trade: TradeEntity)
}
