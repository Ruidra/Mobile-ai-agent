package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ScheduleCategory
import com.example.model.ScheduleEvent
import com.example.ui.AuraViewModel
import com.example.ui.components.VoiceOrb
import com.example.ui.theme.*

@Composable
fun DashboardScreen(
    viewModel: AuraViewModel,
    onOpenVoiceDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scheduleEvents by viewModel.scheduleEvents.collectAsState()
    val marketQuotes by viewModel.marketQuotes.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()
    val statusBanner by viewModel.statusBanner.collectAsState()

    val nextUrgentEvent = scheduleEvents.firstOrNull { it.isUrgent && !it.isCompleted }
    val nextPrivateEvent = scheduleEvents.firstOrNull { it.isPrivate && !it.isCompleted }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Executive Header
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "WELCOME BACK",
                        style = MaterialTheme.typography.labelSmall,
                        color = AuraCyan,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = userProfile.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = AuraTextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${userProfile.role} • ${userProfile.tone.title}",
                        style = MaterialTheme.typography.bodySmall,
                        color = AuraTextSecondary
                    )
                }

                Surface(
                    color = AuraSurfaceElevated,
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AuraCardBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (userProfile.backgroundMonitoring) AuraEmerald else AuraAmber)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (userProfile.backgroundMonitoring) "Active (Lock-Ready)" else "Standby",
                            style = MaterialTheme.typography.labelSmall,
                            color = AuraTextPrimary
                        )
                    }
                }
            }
        }

        // Status Banner Notification
        if (statusBanner != null) {
            item {
                Surface(
                    color = AuraCyanGlow,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AuraCyan.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = AuraCyanBright,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = statusBanner ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = AuraTextPrimary,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = { viewModel.setStatusBanner(null) },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Dismiss",
                                tint = AuraTextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        // Live Voice Centerpiece
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("voice_centerpiece_card"),
                colors = CardDefaults.cardColors(containerColor = AuraSurfaceDark),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, AuraCardBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "AURA AUTONOMOUS CORE",
                        style = MaterialTheme.typography.labelSmall,
                        color = AuraTextMuted,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    VoiceOrb(
                        isListening = false,
                        isSpeaking = isSpeaking,
                        onClick = onOpenVoiceDialog
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = if (isSpeaking) "Aura is Speaking..." else "Tap to Issue Natural Voice Command",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isSpeaking) AuraCyanBright else AuraTextPrimary,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Controls WhatsApp, calendar, files, web search & device apps",
                        style = MaterialTheme.typography.bodySmall,
                        color = AuraTextMuted
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Voice Quick Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.processVoiceCommand("What is my most urgent priority and next private event?") },
                            modifier = Modifier.weight(1f).testTag("quick_priority_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = AuraSurfaceElevated),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.PriorityHigh, contentDescription = null, tint = AuraAmber, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Urgent Priority", style = MaterialTheme.typography.labelSmall, color = AuraTextPrimary)
                        }

                        Button(
                            onClick = { viewModel.optimizeSchedule() },
                            modifier = Modifier.weight(1f).testTag("quick_optimize_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = AuraSurfaceElevated),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.Bolt, contentDescription = null, tint = AuraCyanBright, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Optimize Day", style = MaterialTheme.typography.labelSmall, color = AuraTextPrimary)
                        }
                    }
                }
            }
        }

        // Live Global Markets Ticker
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "LIVE GLOBAL PULSE & MARKETS",
                        style = MaterialTheme.typography.labelSmall,
                        color = AuraTextMuted,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Real-time Feed",
                        style = MaterialTheme.typography.labelSmall,
                        color = AuraCyan
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(marketQuotes) { quote ->
                        Surface(
                            color = AuraSurfaceDark,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, AuraCardBorder)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = quote.symbol,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = AuraTextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = quote.price,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = AuraTextSecondary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = quote.change,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (quote.isPositive) AuraEmerald else AuraRoseUrgent,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Next Urgent / Private Priority Alert Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("urgent_priority_card"),
                colors = CardDefaults.cardColors(
                    containerColor = if (nextUrgentEvent != null) AuraSurfaceDark else AuraSurfaceDark
                ),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (nextUrgentEvent != null) AuraAmber.copy(alpha = 0.6f) else AuraCardBorder
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = AuraAmber.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = AuraAmber, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "NEXT URGENT FOCUS",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = AuraAmber,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        if (nextPrivateEvent != null) {
                            Surface(
                                color = AuraIndigo.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "Private: ${nextPrivateEvent.timeRange}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = AuraIndigo,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = nextUrgentEvent?.title ?: "All high priorities fulfilled",
                        style = MaterialTheme.typography.titleMedium,
                        color = AuraTextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Time: ${nextUrgentEvent?.timeRange ?: "Free itinerary"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = AuraCyanBright
                    )

                    if (nextUrgentEvent?.prepNotes?.isNotBlank() == true) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = nextUrgentEvent.prepNotes,
                            style = MaterialTheme.typography.bodySmall,
                            color = AuraTextSecondary
                        )
                    }

                    if (nextUrgentEvent?.relatedFiles?.isNotEmpty() == true) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.AttachFile, contentDescription = null, tint = AuraTextMuted, modifier = Modifier.size(14.dp))
                            nextUrgentEvent.relatedFiles.forEach { file ->
                                Surface(
                                    color = AuraSurfaceElevated,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = file,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = AuraTextSecondary,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.sendLockScreenUrgentNotification() },
                            modifier = Modifier.weight(1f).testTag("push_lockscreen_test_button"),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, AuraCardBorder)
                        ) {
                            Icon(Icons.Default.LockClock, contentDescription = null, tint = AuraCyanBright, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Lock Screen Alert", style = MaterialTheme.typography.labelSmall, color = AuraTextPrimary)
                        }

                        Button(
                            onClick = {
                                nextUrgentEvent?.let {
                                    viewModel.processVoiceCommand("Synthesize prep notes and briefing for ${it.title}")
                                }
                            },
                            modifier = Modifier.weight(1f).testTag("prep_notes_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = AuraCyanBright),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("AI Prep Briefing", style = MaterialTheme.typography.labelSmall, color = AuraBackgroundDark, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Today's Timeline Section Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarToday, contentDescription = null, tint = AuraCyan, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "TODAY'S SMART TIMELINE",
                        style = MaterialTheme.typography.titleMedium,
                        color = AuraTextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                TextButton(
                    onClick = { viewModel.optimizeSchedule() },
                    modifier = Modifier.testTag("optimize_schedule_link")
                ) {
                    Icon(Icons.Default.AutoFixHigh, contentDescription = null, tint = AuraCyanBright, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Auto-Optimize", color = AuraCyanBright, style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        // Timeline Items
        items(scheduleEvents) { event ->
            ScheduleEventCard(
                event = event,
                onToggleCompleted = { viewModel.toggleScheduleEvent(event.id) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ScheduleEventCard(
    event: ScheduleEvent,
    onToggleCompleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryColor = when (event.category) {
        ScheduleCategory.MEETING -> AuraCyan
        ScheduleCategory.FOCUS_WORK -> AuraIndigo
        ScheduleCategory.HEALTH_BREAK -> AuraEmerald
        ScheduleCategory.PRIVATE_EVENT -> AuraAmber
        ScheduleCategory.WHATSAPP_CALL -> Color(0xFF25D366) // WhatsApp green
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("schedule_card_${event.id}"),
        colors = CardDefaults.cardColors(
            containerColor = if (event.isCompleted) AuraSurfaceDark.copy(alpha = 0.6f) else AuraSurfaceDark
        ),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, AuraCardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category color bar
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(48.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(categoryColor)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = event.timeRange,
                        style = MaterialTheme.typography.labelSmall,
                        color = AuraTextSecondary,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (event.isPrivate) {
                        Surface(
                            color = AuraAmber.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "PRIVATE",
                                style = MaterialTheme.typography.labelSmall,
                                color = AuraAmber,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                    if (event.isUrgent) {
                        Surface(
                            color = AuraRoseUrgent.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "URGENT",
                                style = MaterialTheme.typography.labelSmall,
                                color = AuraRoseUrgent,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (event.isCompleted) AuraTextMuted else AuraTextPrimary,
                    fontWeight = FontWeight.Medium
                )

                if (event.prepNotes.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = event.prepNotes,
                        style = MaterialTheme.typography.bodySmall,
                        color = AuraTextMuted,
                        maxLines = 2
                    )
                }
            }

            IconButton(
                onClick = onToggleCompleted,
                modifier = Modifier.testTag("toggle_event_${event.id}")
            ) {
                Icon(
                    imageVector = if (event.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                    contentDescription = "Toggle Complete",
                    tint = if (event.isCompleted) AuraEmerald else AuraTextMuted
                )
            }
        }
    }
}
