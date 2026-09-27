package com.lifeos.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lifeos.core.navigation.LifeOsDestination
import com.lifeos.feature.assistant.presentation.AssistantRoute
import com.lifeos.feature.dashboard.presentation.DashboardRoute
import com.lifeos.feature.learning.presentation.LearningRoute
import com.lifeos.feature.reminders.presentation.RemindersRoute
import com.lifeos.feature.tradingjournal.presentation.TradingRoute
import com.lifeos.feature.travel.presentation.TravelRoute

sealed class BottomNavItem(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val destination: LifeOsDestination
) {
    data object Home : BottomNavItem("Hub", Icons.Default.Home, LifeOsDestination.Dashboard)
    data object Tasks : BottomNavItem("Tasks", Icons.Default.EventNote, LifeOsDestination.Reminders)
    data object Trading : BottomNavItem("Trade", Icons.Default.ShowChart, LifeOsDestination.TradingJournal)
    data object Learn : BottomNavItem("Learn", Icons.Default.School, LifeOsDestination.Learning)
    data object Travel : BottomNavItem("Travel", Icons.Default.FlightTakeoff, LifeOsDestination.Travel)
    data object Copilot : BottomNavItem("AI", Icons.Default.AutoAwesome, LifeOsDestination.AiAssistant)
}

@Composable
fun LifeOsAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Tasks,
        BottomNavItem.Trading,
        BottomNavItem.Learn,
        BottomNavItem.Travel,
        BottomNavItem.Copilot
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar {
                items.forEach { item ->
                    val isSelected = currentRoute == item.destination::class.qualifiedName
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = isSelected,
                        onClick = {
                            navController.navigate(item.destination) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = LifeOsDestination.Dashboard,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<LifeOsDestination.Dashboard> {
                DashboardRoute(
                    onNavigateToReminders = { navController.navigate(LifeOsDestination.Reminders) },
                    onNavigateToTrading = { navController.navigate(LifeOsDestination.TradingJournal) },
                    onNavigateToLearning = { navController.navigate(LifeOsDestination.Learning) },
                    onNavigateToTravel = { navController.navigate(LifeOsDestination.Travel) },
                    onNavigateToAssistant = { navController.navigate(LifeOsDestination.AiAssistant) }
                )
            }
            composable<LifeOsDestination.Reminders> {
                RemindersRoute()
            }
            composable<LifeOsDestination.TradingJournal> {
                TradingRoute()
            }
            composable<LifeOsDestination.Learning> {
                LearningRoute()
            }
            composable<LifeOsDestination.Travel> {
                TravelRoute()
            }
            composable<LifeOsDestination.AiAssistant> {
                AssistantRoute()
            }
        }
    }
}
