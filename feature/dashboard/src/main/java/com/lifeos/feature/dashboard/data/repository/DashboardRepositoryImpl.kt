package com.lifeos.feature.dashboard.data.repository

import com.lifeos.core.database.dao.LearningDao
import com.lifeos.core.database.dao.ReminderDao
import com.lifeos.core.database.dao.TradeDao
import com.lifeos.core.database.dao.TravelDao
import com.lifeos.core.model.DashboardSummary
import com.lifeos.feature.dashboard.domain.repository.DashboardRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class DashboardRepositoryImpl @Inject constructor(
    private val reminderDao: ReminderDao,
    private val tradeDao: TradeDao,
    private val learningDao: LearningDao,
    private val travelDao: TravelDao
) : DashboardRepository {

    override fun getDashboardSummary(): Flow<DashboardSummary> =
        combine(
            reminderDao.getPendingReminders(),
            tradeDao.getAllTrades(),
            learningDao.getActiveGoalsCount(),
            travelDao.getNextUpcomingTrip()
        ) { reminders, tradeEntities, activeGoalsCount, nextTrip ->
            val domainTrades = tradeEntities.map { it.toDomainModel() }
            val closedTrades = domainTrades.filter { it.realizedPnL != null }
            val totalPnL = closedTrades.sumOf { it.realizedPnL ?: 0.0 }
            val openTradesCount = domainTrades.count { it.status.name == "OPEN" }

            val briefing = if (reminders.isNotEmpty()) {
                "You have ${reminders.size} pending tasks today. Focus on high priority items and maintain risk discipline in open positions."
            } else {
                "All clear for today! Take time to explore new learning topics or plan upcoming journeys."
            }

            DashboardSummary(
                pendingRemindersCount = reminders.size,
                upcomingUrgentReminders = reminders.take(3).map { it.toDomainModel() },
                openTradesCount = openTradesCount,
                totalRealizedPnL = totalPnL,
                activeLearningGoalsCount = activeGoalsCount,
                upcomingTravelDestination = nextTrip?.destination,
                aiDailyBriefing = briefing
            )
        }
}
