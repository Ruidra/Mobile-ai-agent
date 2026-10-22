package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.model.VoiceCommandState
import com.example.ui.theme.*

@Composable
fun VoiceCommandDialog(
    voiceState: VoiceCommandState,
    isSpeaking: Boolean,
    onDismiss: () -> Unit,
    onSubmitCommand: (String) -> Unit,
    onStopSpeaking: () -> Unit
) {
    var manualInput by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, AuraCardBorder, RoundedCornerShape(20.dp))
                .testTag("voice_command_dialog"),
            color = AuraSurfaceDark,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(if (voiceState.isProcessing) AuraAmber else AuraCyanBright)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (voiceState.isProcessing) "Aura Neural Processing..." else "Aura Voice Command",
                            style = MaterialTheme.typography.titleMedium,
                            color = AuraTextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_voice_dialog_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Dialog",
                            tint = AuraTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Orb Animation in Dialog
                VoiceOrb(
                    isListening = voiceState.isProcessing,
                    isSpeaking = isSpeaking,
                    onClick = { /* trigger or test */ }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Transcript
                if (voiceState.transcript.isNotBlank()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = AuraSurfaceElevated),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "YOU SAID",
                                style = MaterialTheme.typography.labelSmall,
                                color = AuraTextMuted,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "\"${voiceState.transcript}\"",
                                style = MaterialTheme.typography.bodyMedium,
                                color = AuraCyanBright
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // AI Response
                if (voiceState.isProcessing) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(vertical = 12.dp)
                    ) {
                        CircularProgressIndicator(
                            color = AuraCyanBright,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Synthesizing executive plan...",
                            style = MaterialTheme.typography.bodySmall,
                            color = AuraTextSecondary
                        )
                    }
                } else if (voiceState.responseText.isNotBlank()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = AuraBackgroundDark),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AuraCardBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "AURA EXECUTIVE RESPONSE",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = AuraCyan,
                                    fontWeight = FontWeight.Bold
                                )
                                if (isSpeaking) {
                                    IconButton(
                                        onClick = onStopSpeaking,
                                        modifier = Modifier.size(28.dp).testTag("stop_speech_button")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Stop,
                                            contentDescription = "Stop Speech",
                                            tint = AuraRoseUrgent
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = voiceState.responseText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = AuraTextPrimary,
                                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
                            )

                            if (voiceState.actionTaken != null) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Surface(
                                    color = AuraEmerald.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "⚡ Action: ${voiceState.actionTaken}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = AuraEmerald,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Manual Command Input Field & Quick Prompts
                OutlinedTextField(
                    value = manualInput,
                    onValueChange = { manualInput = it },
                    placeholder = { Text("Or type voice directive...", color = AuraTextMuted) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("voice_command_input"),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                if (manualInput.isNotBlank()) {
                                    onSubmitCommand(manualInput)
                                    manualInput = ""
                                }
                            },
                            modifier = Modifier.testTag("submit_voice_command_button")
                        ) {
                            Icon(Icons.Default.Send, contentDescription = "Send Command", tint = AuraCyanBright)
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AuraCyanBright,
                        unfocusedBorderColor = AuraCardBorder,
                        focusedTextColor = AuraTextPrimary,
                        unfocusedTextColor = AuraTextPrimary,
                        cursorColor = AuraCyanBright
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Suggestion chips
                Text(
                    text = "QUICK DIRECTIVES",
                    style = MaterialTheme.typography.labelSmall,
                    color = AuraTextMuted,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(8.dp))

                val suggestions = listOf(
                    "What's my urgent priority?",
                    "Optimize today's schedule",
                    "WhatsApp video call with leads",
                    "Search web for latest AI news",
                    "Make an AI agent like you"
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    suggestions.forEach { suggestion ->
                        Surface(
                            onClick = { onSubmitCommand(suggestion) },
                            color = AuraSurfaceElevated,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("suggestion_chip_${suggestion.take(10)}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = null,
                                    tint = AuraCyan,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = suggestion,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AuraTextPrimary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
