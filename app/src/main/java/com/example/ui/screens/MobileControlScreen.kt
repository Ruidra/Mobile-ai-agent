package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.ui.AuraViewModel
import com.example.ui.theme.*

@Composable
fun MobileControlScreen(
    viewModel: AuraViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()

    var selectedWhatsAppContact by remember { mutableStateOf("Elena Rostova (EU Lead)") }
    var quickWhatsAppMessage by remember { mutableStateOf("Connecting via Aura AI: reviewing roadmap milestones now.") }

    val contacts = listOf(
        "Elena Rostova (EU Lead)",
        "Sarah Lin (CFO)",
        "David Chen (CTO)",
        "Marcus Vance (Apex Ventures)",
        "Family"
    )

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
                        text = "MOBILE OS & HARDWARE HUB",
                        style = MaterialTheme.typography.labelSmall,
                        color = AuraCyan,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "App & Device Automation",
                        style = MaterialTheme.typography.titleLarge,
                        color = AuraTextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Surface(
                    color = AuraEmerald.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = "Device Paired",
                        style = MaterialTheme.typography.labelSmall,
                        color = AuraEmerald,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // WhatsApp Automation Suite Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("whatsapp_control_card"),
                colors = CardDefaults.cardColors(containerColor = AuraSurfaceDark),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF25D366).copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF25D366).copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Phone, contentDescription = null, tint = Color(0xFF25D366), modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "WHATSAPP COMMAND CENTER",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = AuraTextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Natural language voice & video call integration",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AuraTextMuted
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "SELECT CONTACT",
                        style = MaterialTheme.typography.labelSmall,
                        color = AuraTextMuted,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        contacts.take(3).forEach { contact ->
                            FilterChip(
                                selected = selectedWhatsAppContact == contact,
                                onClick = { selectedWhatsAppContact = contact },
                                label = { Text(contact.substringBefore(" "), style = MaterialTheme.typography.bodySmall) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF25D366),
                                    selectedLabelColor = AuraBackgroundDark
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Call Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                viewModel.triggerWhatsAppCall(selectedWhatsAppContact, isVideo = false)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f).testTag("whatsapp_voice_call_button")
                        ) {
                            Icon(Icons.Default.Call, contentDescription = null, tint = AuraBackgroundDark, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Voice Call", color = AuraBackgroundDark, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                        }

                        Button(
                            onClick = {
                                viewModel.triggerWhatsAppCall(selectedWhatsAppContact, isVideo = true)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AuraIndigo),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f).testTag("whatsapp_video_call_button")
                        ) {
                            Icon(Icons.Default.Videocam, contentDescription = null, tint = AuraTextPrimary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Video Call", color = AuraTextPrimary, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Message Field
                    OutlinedTextField(
                        value = quickWhatsAppMessage,
                        onValueChange = { quickWhatsAppMessage = it },
                        placeholder = { Text("Draft WhatsApp voice note or message...", color = AuraTextMuted) },
                        modifier = Modifier.fillMaxWidth().testTag("whatsapp_message_input"),
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    viewModel.triggerWhatsAppMessage(selectedWhatsAppContact, quickWhatsAppMessage)
                                },
                                modifier = Modifier.testTag("send_whatsapp_msg_button")
                            ) {
                                Icon(Icons.Default.Send, contentDescription = "Send", tint = Color(0xFF25D366))
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF25D366),
                            unfocusedBorderColor = AuraCardBorder,
                            focusedTextColor = AuraTextPrimary,
                            unfocusedTextColor = AuraTextPrimary
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }
        }

        // Background & Lock Screen Intelligence
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("lock_screen_automation_card"),
                colors = CardDefaults.cardColors(containerColor = AuraSurfaceDark),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, AuraCardBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "BACKGROUND & LOCK SCREEN ENGINE",
                                style = MaterialTheme.typography.titleMedium,
                                color = AuraTextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Operates when phone is locked or app minimized",
                                style = MaterialTheme.typography.bodySmall,
                                color = AuraTextMuted
                            )
                        }

                        Switch(
                            checked = userProfile.backgroundMonitoring,
                            onCheckedChange = { viewModel.toggleBackgroundMonitoring(it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = AuraCyanBright, checkedTrackColor = AuraSurfaceElevated),
                            modifier = Modifier.testTag("background_monitoring_switch")
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Aura runs an autonomous sentinel thread that monitors pending meeting times, incoming messages, and market shifts. When an urgent event approaches, it pushes high-priority interactive heads-up alerts directly over the lock screen.",
                        style = MaterialTheme.typography.bodySmall,
                        color = AuraTextSecondary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { viewModel.sendLockScreenUrgentNotification() },
                        colors = ButtonDefaults.buttonColors(containerColor = AuraCyanBright),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().testTag("simulate_lockscreen_notification_btn")
                    ) {
                        Icon(Icons.Default.NotificationImportant, contentDescription = null, tint = AuraBackgroundDark)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Trigger Live Lock-Screen Priority Push", color = AuraBackgroundDark, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // System App Launchers Matrix
        item {
            Text(
                text = "DEVICE APPLICATION CONTROLLERS",
                style = MaterialTheme.typography.labelSmall,
                color = AuraTextMuted,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            val apps = listOf(
                Triple("CAMERA", "Camera & Docs", Icons.Default.CameraAlt),
                Triple("SETTINGS", "System Settings", Icons.Default.Settings),
                Triple("ALARMS", "Clock & Alarms", Icons.Default.Alarm),
                Triple("BROWSER", "Web Browser", Icons.Default.Language)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                apps.forEach { (action, name, icon) ->
                    Surface(
                        onClick = { viewModel.openApp(action) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("app_launcher_$action"),
                        color = AuraSurfaceDark,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AuraCardBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(icon, contentDescription = name, tint = AuraCyanBright, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = name,
                                style = MaterialTheme.typography.labelSmall,
                                color = AuraTextPrimary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
