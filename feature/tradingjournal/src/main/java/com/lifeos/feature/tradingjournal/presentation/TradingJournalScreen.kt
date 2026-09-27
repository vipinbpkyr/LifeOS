package com.lifeos.feature.tradingjournal.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lifeos.core.designsystem.component.AiBadge
import com.lifeos.core.designsystem.component.LifeOsCard
import com.lifeos.core.designsystem.component.MetricCard
import com.lifeos.core.designsystem.theme.LifeOsSuccess
import com.lifeos.core.designsystem.theme.LifeOsTheme
import com.lifeos.core.model.TradeEmotion
import com.lifeos.core.model.TradeEntry
import com.lifeos.core.model.TradeStatus
import com.lifeos.core.model.TradeType
import com.lifeos.feature.tradingjournal.domain.usecase.TradingStats

@Composable
fun TradingRoute(
    viewModel: TradingViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TradingJournalScreen(
        uiState = uiState,
        onTickerChanged = viewModel::onTickerChanged,
        onEntryPriceChanged = viewModel::onEntryPriceChanged,
        onLogTrade = viewModel::logNewTrade,
        modifier = modifier
    )
}

@Composable
fun TradingJournalScreen(
    uiState: TradingUiState,
    onTickerChanged: (String) -> Unit,
    onEntryPriceChanged: (String) -> Unit,
    onLogTrade: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = modifier.fillMaxSize()) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Trading Journal",
                    style = MaterialTheme.typography.headlineMedium
                )
                AiBadge(text = "R:R Optimizer")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Stats Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(
                    title = "Open Trades",
                    value = "${uiState.stats.openTradesCount}",
                    icon = Icons.Default.ShowChart,
                    accentColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Win Rate",
                    value = "${String.format("%.1f", uiState.stats.winRatePercentage)}%",
                    icon = Icons.Default.TrendingUp,
                    accentColor = LifeOsSuccess,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Log Trade
            LifeOsCard {
                Column {
                    Text(
                        text = "Quick Log Position",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = uiState.tickerInput,
                            onValueChange = onTickerChanged,
                            placeholder = { Text("Ticker (e.g. NVDA)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = uiState.entryPriceInput,
                            onValueChange = onEntryPriceChanged,
                            placeholder = { Text("Price ($)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = onLogTrade,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Log Trade Entry")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Trade History",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (uiState.trades.isEmpty()) {
                LifeOsCard {
                    Text(
                        text = "No trades logged yet. Track your edge and let AI analyze your emotions!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.trades, key = { it.id }) { trade ->
                        TradeItemCard(trade = trade)
                    }
                }
            }
        }
    }
}

@Composable
private fun TradeItemCard(trade: TradeEntry) {
    LifeOsCard {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${trade.tickerSymbol} (${trade.tradeType.name})",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = trade.status.name,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Entry: \$${trade.entryPrice} • Qty: ${trade.quantity} • Target: \$${trade.takeProfit}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Psychology: ${trade.emotion} • R:R: ${String.format("%.2f", trade.riskRewardRatio)}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TradingJournalScreenPreview() {
    LifeOsTheme {
        TradingJournalScreen(
            uiState = TradingUiState(
                trades = listOf(
                    TradeEntry(
                        id = "1",
                        tickerSymbol = "NVDA",
                        entryPrice = 120.0,
                        quantity = 50.0,
                        stopLoss = 115.0,
                        takeProfit = 135.0,
                        tradeType = TradeType.BUY_LONG,
                        entryTimestamp = 1700000000000L,
                        status = TradeStatus.OPEN,
                        emotion = TradeEmotion.CONFIDENT
                    )
                ),
                stats = TradingStats(
                    totalTrades = 12,
                    openTradesCount = 1,
                    totalRealizedPnL = 3450.0,
                    winRatePercentage = 66.7
                ),
                tickerInput = "NVDA",
                entryPriceInput = "120.0"
            ),
            onTickerChanged = {},
            onEntryPriceChanged = {},
            onLogTrade = {}
        )
    }
}
