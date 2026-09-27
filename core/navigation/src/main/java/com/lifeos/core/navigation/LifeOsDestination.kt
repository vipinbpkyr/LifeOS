package com.lifeos.core.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface LifeOsDestination {

    @Serializable
    data object Dashboard : LifeOsDestination

    @Serializable
    data object Reminders : LifeOsDestination

    @Serializable
    data object TradingJournal : LifeOsDestination

    @Serializable
    data object Learning : LifeOsDestination

    @Serializable
    data object Travel : LifeOsDestination

    @Serializable
    data object AiAssistant : LifeOsDestination

    @Serializable
    data class ReminderDetail(val reminderId: String, val test: Test) : LifeOsDestination
}
