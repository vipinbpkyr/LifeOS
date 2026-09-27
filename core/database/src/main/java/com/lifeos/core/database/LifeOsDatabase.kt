package com.lifeos.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lifeos.core.database.dao.AiMessageDao
import com.lifeos.core.database.dao.LearningDao
import com.lifeos.core.database.dao.ReminderDao
import com.lifeos.core.database.dao.TradeDao
import com.lifeos.core.database.dao.TravelDao
import com.lifeos.core.database.entity.AiMessageEntity
import com.lifeos.core.database.entity.LearningGoalEntity
import com.lifeos.core.database.entity.ReminderEntity
import com.lifeos.core.database.entity.TradeEntity
import com.lifeos.core.database.entity.TravelPlanEntity

@Database(
    entities = [
        ReminderEntity::class,
        TradeEntity::class,
        LearningGoalEntity::class,
        TravelPlanEntity::class,
        AiMessageEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class LifeOsDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao
    abstract fun tradeDao(): TradeDao
    abstract fun learningDao(): LearningDao
    abstract fun travelDao(): TravelDao
    abstract fun aiMessageDao(): AiMessageDao
}
