package com.lifeos.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lifeos.core.database.entity.LearningGoalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LearningDao {
    @Query("SELECT * FROM learning_goals ORDER BY createdAt DESC")
    fun getAllGoals(): Flow<List<LearningGoalEntity>>

    @Query("SELECT * FROM learning_goals WHERE id = :id")
    suspend fun getGoalById(id: String): LearningGoalEntity?

    @Query("SELECT COUNT(*) FROM learning_goals WHERE progressPercentage < 100")
    fun getActiveGoalsCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: LearningGoalEntity)

    @Update
    suspend fun updateGoal(goal: LearningGoalEntity)

    @Delete
    suspend fun deleteGoal(goal: LearningGoalEntity)
}
