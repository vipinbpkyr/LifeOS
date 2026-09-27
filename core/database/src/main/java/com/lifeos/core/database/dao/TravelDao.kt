package com.lifeos.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lifeos.core.database.entity.TravelPlanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TravelDao {
    @Query("SELECT * FROM travel_plans ORDER BY startDate ASC")
    fun getAllTravelPlans(): Flow<List<TravelPlanEntity>>

    @Query("SELECT * FROM travel_plans WHERE id = :id")
    suspend fun getTravelPlanById(id: String): TravelPlanEntity?

    @Query("SELECT * FROM travel_plans WHERE startDate >= :currentTimestamp ORDER BY startDate ASC LIMIT 1")
    fun getNextUpcomingTrip(currentTimestamp: Long = System.currentTimeMillis()): Flow<TravelPlanEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTravelPlan(plan: TravelPlanEntity)

    @Update
    suspend fun updateTravelPlan(plan: TravelPlanEntity)

    @Delete
    suspend fun deleteTravelPlan(plan: TravelPlanEntity)
}
