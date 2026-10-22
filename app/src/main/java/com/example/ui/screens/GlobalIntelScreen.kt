package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.AuraViewModel
import com.example.ui.theme.*

@Composable
fun GlobalIntelScreen(
    viewModel: AuraViewModel,
    modifier: Modifier = Modifier
) {
    val marketQuotes by viewModel.marketQuotes.collectAsState()
    val newsItems by viewModel.newsItems.collectAsState()
    val socialTrends by viewModel.socialTrends.collectAsState()
    val webSearchQuery by viewModel.webSearchQuery.collectAsState()
    val webSearchResults by viewModel.webSearchResults.collectAsState()
    val isSearchingWeb by viewModel.isSearchingWeb.collectAsState()

    var activeSubTab by remember { mutableStateOf(0) } // 0: Web Search, 1: Markets, 2: News, 3: Trends

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "GLOBAL INTELLIGENCE & RESEARCH",
                        style = MaterialTheme.typography.labelSmall,
                        color = AuraCyan,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Real-Time World Pulse",
                        style = MaterialTheme.typography.titleLarge,
                        color = AuraTextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Surface(
                    color = AuraCyanGlow,
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AuraCyan.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = "Live Stream",
                        style = MaterialTheme.typography.labelSmall,
                        color = AuraCyanBright,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Sub Tabs
        item {
            TabRow(
                selectedTabIndex = activeSubTab,
                containerColor = AuraSurfaceDark,
                contentColor = AuraCyanBright,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[activeSubTab]),
                        color = AuraCyanBright
                    )
                }
            ) {
                Tab(
                    selected = activeSubTab == 0,
                    onClick = { activeSubTab = 0 },
                    text = { Text("Web Search", style = MaterialTheme.typography.bodySmall) }
                )
                Tab(
                    selected = activeSubTab == 1,
                    onClick = { activeSubTab = 1 },
                    text = { Text("Markets", style = MaterialTheme.typography.bodySmall) }
                )
                Tab(
                    selected = activeSubTab == 2,
                    onClick = { activeSubTab = 2 },
                    text = { Text("World News", style = MaterialTheme.typography.bodySmall) }
                )
                Tab(
                    selected = activeSubTab == 3,
                    onClick = { activeSubTab = 3 },
                    text = { Text("Social Trends", style = MaterialTheme.typography.bodySmall) }
                )
            }
        }

        // Sub Tab Content: 0 Web Search
        if (activeSubTab == 0) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = AuraSurfaceDark),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AuraCardBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "AUTONOMOUS WEB & DOCUMENT SEARCH",
                            style = MaterialTheme.typography.labelSmall,
                            color = AuraCyanBright,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = webSearchQuery,
                                onValueChange = { viewModel.updateSearchQuery(it) },
                                placeholder = { Text("Ask Aura to research the web or find documents...", color = AuraTextMuted, style = MaterialTheme.typography.bodySmall) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("web_search_input"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = AuraCyanBright,
                                    unfocusedBorderColor = AuraCardBorder,
                                    focusedTextColor = AuraTextPrimary,
                                    unfocusedTextColor = AuraTextPrimary
                                ),
                                shape = RoundedCornerShape(10.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = { viewModel.executeWebSearch(webSearchQuery) },
                                colors = ButtonDefaults.buttonColors(containerColor = AuraCyanBright),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.testTag("execute_web_search_button")
                            ) {
                                Icon(Icons.Default.TravelExplore, contentDescription = "Search", tint = AuraBackgroundDark)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Quick queries
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf("AI Agent Benchmarks", "Q4 Global Tech IPOs", "Series B Governance").forEach { topic ->
                                Surface(
                                    onClick = {
                                        viewModel.updateSearchQuery(topic)
                                        viewModel.executeWebSearch(topic)
                                    },
                                    color = AuraSurfaceElevated,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = topic,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = AuraTextSecondary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (isSearchingWeb) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(color = AuraCyanBright, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Aura is scouring live web feeds and documents...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = AuraTextSecondary
                        )
                    }
                }
            } else if (webSearchResults != null) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("web_search_results_card"),
                        colors = CardDefaults.cardColors(containerColor = AuraSurfaceDark),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AuraCyan.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "SYNTHESIZED EXECUTIVE BRIEFING",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = AuraCyanBright,
                                    fontWeight = FontWeight.Bold
                                )
                                IconButton(
                                    onClick = { viewModel.speakText(webSearchResults ?: "") },
                                    modifier = Modifier.size(28.dp).testTag("read_search_result_button")
                                ) {
                                    Icon(Icons.Default.VolumeUp, contentDescription = "Read Aloud", tint = AuraCyanBright)
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = webSearchResults ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                color = AuraTextPrimary,
                                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
                            )
                        }
                    }
                }
            }
        }

        // Sub Tab Content: 1 Markets
        if (activeSubTab == 1) {
            items(marketQuotes) { quote ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = AuraSurfaceDark),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AuraCardBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = quote.symbol,
                                style = MaterialTheme.typography.titleMedium,
                                color = AuraTextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${quote.name} • ${quote.category}",
                                style = MaterialTheme.typography.bodySmall,
                                color = AuraTextMuted
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = quote.price,
                                style = MaterialTheme.typography.titleMedium,
                                color = AuraTextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = quote.change,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (quote.isPositive) AuraEmerald else AuraRoseUrgent,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Sub Tab Content: 2 News
        if (activeSubTab == 2) {
            items(newsItems) { news ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = AuraSurfaceDark),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AuraCardBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Surface(
                                color = AuraIndigo.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = news.category.uppercase(),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = AuraIndigo,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = "${news.source} • ${news.timeAgo}",
                                style = MaterialTheme.typography.labelSmall,
                                color = AuraTextMuted
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = news.headline,
                            style = MaterialTheme.typography.titleMedium,
                            color = AuraTextPrimary,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = news.aiSummary,
                            style = MaterialTheme.typography.bodyMedium,
                            color = AuraTextSecondary
                        )
                    }
                }
            }
        }

        // Sub Tab Content: 3 Social Trends
        if (activeSubTab == 3) {
            items(socialTrends) { trend ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = AuraSurfaceDark),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AuraCardBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = trend.topic,
                                style = MaterialTheme.typography.titleMedium,
                                color = AuraCyanBright,
                                fontWeight = FontWeight.Bold
                            )
                            Surface(
                                color = AuraEmerald.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = trend.sentiment,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = AuraEmerald,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Volume: ${trend.volume}",
                            style = MaterialTheme.typography.bodySmall,
                            color = AuraTextMuted
                        )

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = trend.summary,
                            style = MaterialTheme.typography.bodyMedium,
                            color = AuraTextSecondary
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
