package com.lifeos.core.database.di

import android.content.Context
import androidx.room.Room
import com.lifeos.core.database.LifeOsDatabase
import com.lifeos.core.database.dao.AiMessageDao
import com.lifeos.core.database.dao.LearningDao
import com.lifeos.core.database.dao.ReminderDao
import com.lifeos.core.database.dao.TradeDao
import com.lifeos.core.database.dao.TravelDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): LifeOsDatabase = Room.databaseBuilder(
        context,
        LifeOsDatabase::class.java,
        "lifeos_database.db"
    ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideReminderDao(database: LifeOsDatabase): ReminderDao = database.reminderDao()

    @Provides
    fun provideTradeDao(database: LifeOsDatabase): TradeDao = database.tradeDao()

    @Provides
    fun provideLearningDao(database: LifeOsDatabase): LearningDao = database.learningDao()

    @Provides
    fun provideTravelDao(database: LifeOsDatabase): TravelDao = database.travelDao()

    @Provides
    fun provideAiMessageDao(database: LifeOsDatabase): AiMessageDao = database.aiMessageDao()
}
