package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.ExecutiveTone
import com.example.ui.AuraViewModel
import com.example.ui.theme.*

@Composable
fun SettingsScreen(
    viewModel: AuraViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()

    var voicePitch by remember { mutableStateOf(userProfile.voicePitch) }
    var voiceRate by remember { mutableStateOf(userProfile.voiceRate) }

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
                        text = "EXECUTIVE IDENTITY & PREFERENCES",
                        style = MaterialTheme.typography.labelSmall,
                        color = AuraCyan,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Personal Style & Directives",
                        style = MaterialTheme.typography.titleLarge,
                        color = AuraTextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Surface(
                    color = AuraCyanGlow,
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = "Tuned to ${userProfile.name}",
                        style = MaterialTheme.typography.labelSmall,
                        color = AuraCyanBright,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Executive Tone Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("executive_tone_settings_card"),
                colors = CardDefaults.cardColors(containerColor = AuraSurfaceDark),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, AuraCardBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "EXECUTIVE COMMUNICATION STYLE",
                        style = MaterialTheme.typography.labelSmall,
                        color = AuraCyanBright,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Aura adapts email replies, scheduling notes, and spoken briefings to match this persona exactly.",
                        style = MaterialTheme.typography.bodySmall,
                        color = AuraTextSecondary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        ExecutiveTone.values().forEach { tone ->
                            val isSelected = userProfile.tone == tone
                            Surface(
                                onClick = { viewModel.updateTone(tone) },
                                color = if (isSelected) AuraSurfaceElevated else AuraBackgroundDark,
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) AuraCyan else AuraCardBorder
                                ),
                                modifier = Modifier.fillMaxWidth().testTag("tone_option_${tone.name}")
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { viewModel.updateTone(tone) },
                                        colors = RadioButtonDefaults.colors(selectedColor = AuraCyanBright)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = tone.title,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = AuraTextPrimary,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = tone.promptInstruction,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = AuraTextMuted
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Voice Acoustics & Pitch
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("voice_acoustics_card"),
                colors = CardDefaults.cardColors(containerColor = AuraSurfaceDark),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, AuraCardBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "NEURAL VOICE ACOUSTICS",
                        style = MaterialTheme.typography.labelSmall,
                        color = AuraCyanBright,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Voice Pitch (${"%.2f".format(voicePitch)}x)",
                        style = MaterialTheme.typography.bodySmall,
                        color = AuraTextPrimary
                    )
                    Slider(
                        value = voicePitch,
                        onValueChange = {
                            voicePitch = it
                            viewModel.updateVoiceTuning(voicePitch, voiceRate)
                        },
                        valueRange = 0.7f..1.4f,
                        colors = SliderDefaults.colors(thumbColor = AuraCyanBright, activeTrackColor = AuraCyan)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Speech Rate (${"%.2f".format(voiceRate)}x)",
                        style = MaterialTheme.typography.bodySmall,
                        color = AuraTextPrimary
                    )
                    Slider(
                        value = voiceRate,
                        onValueChange = {
                            voiceRate = it
                            viewModel.updateVoiceTuning(voicePitch, voiceRate)
                        },
                        valueRange = 0.8f..1.5f,
                        colors = SliderDefaults.colors(thumbColor = AuraCyanBright, activeTrackColor = AuraCyan)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            viewModel.speakText("Greetings ${userProfile.name}. Aura voice engine calibrated to your executive style.")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AuraIndigo),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().testTag("test_voice_button")
                    ) {
                        Icon(Icons.Default.VolumeUp, contentDescription = null, tint = AuraTextPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Audition Voice Profile", color = AuraTextPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // VIP Contacts & Whitelist
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("vip_contacts_card"),
                colors = CardDefaults.cardColors(containerColor = AuraSurfaceDark),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, AuraCardBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "VIP CONTACTS & CALL OVERRIDE",
                        style = MaterialTheme.typography.labelSmall,
                        color = AuraCyanBright,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Calls and urgent messages from VIP contacts bypass quiet hours and trigger lock-screen interrupts.",
                        style = MaterialTheme.typography.bodySmall,
                        color = AuraTextSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    userProfile.vipContacts.forEach { contact ->
                        Surface(
                            color = AuraSurfaceElevated,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = AuraCyan, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = contact,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = AuraTextPrimary,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }

        // Signatures & Disclaimers
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = AuraSurfaceDark),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, AuraCardBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "AUTONOMOUS OUTGOING SIGN-OFF",
                        style = MaterialTheme.typography.labelSmall,
                        color = AuraCyanBright,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = userProfile.autoSignoff,
                        style = MaterialTheme.typography.bodySmall,
                        color = AuraTextSecondary
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
