package com.example.ui.screens

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.model.CreationType
import com.example.model.GeneratedCreation
import com.example.ui.AuraViewModel
import com.example.ui.theme.*

@Composable
fun MakerStudioScreen(
    viewModel: AuraViewModel,
    modifier: Modifier = Modifier
) {
    val creations by viewModel.creations.collectAsState()
    val selectedCreation by viewModel.selectedCreation.collectAsState()
    val isGenerating by viewModel.isGeneratingCreation.collectAsState()
    val makerPrompt by viewModel.makerPrompt.collectAsState()

    var selectedType by remember { mutableStateOf(CreationType.WEBSITE) }
    var showRawCode by remember { mutableStateOf(false) }

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
                        text = "AUTONOMOUS MAKER STUDIO",
                        style = MaterialTheme.typography.labelSmall,
                        color = AuraCyan,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Web, App & Agent Creator",
                        style = MaterialTheme.typography.titleLarge,
                        color = AuraTextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Surface(
                    color = AuraIndigo.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = "Gemini Powered",
                        style = MaterialTheme.typography.labelSmall,
                        color = AuraIndigo,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Creator Prompt Input Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("maker_generator_card"),
                colors = CardDefaults.cardColors(containerColor = AuraSurfaceDark),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, AuraCardBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "WHAT WOULD YOU LIKE AURA TO BUILD?",
                        style = MaterialTheme.typography.labelSmall,
                        color = AuraCyanBright,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Type Selector Chips
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        CreationType.values().forEach { type ->
                            FilterChip(
                                selected = selectedType == type,
                                onClick = { selectedType = type },
                                label = {
                                    Text(
                                        text = when (type) {
                                            CreationType.WEBSITE -> "Website / Portal"
                                            CreationType.ANDROID_APP -> "Mobile App"
                                            CreationType.AI_AGENT -> "AI Sub-Agent"
                                        },
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = AuraCyanBright,
                                    selectedLabelColor = AuraBackgroundDark
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = makerPrompt,
                        onValueChange = { viewModel.setMakerPrompt(it) },
                        placeholder = {
                            Text(
                                text = when (selectedType) {
                                    CreationType.WEBSITE -> "e.g. Build an executive portfolio for Shrihan Rudra Biswas with clean glass cards..."
                                    CreationType.ANDROID_APP -> "e.g. Create a real-time multi-agent orchestrator app with voice..."
                                    CreationType.AI_AGENT -> "e.g. Create an autonomous agent like Aura for legal contract analysis..."
                                },
                                color = AuraTextMuted,
                                style = MaterialTheme.typography.bodySmall
                            )
                        },
                        modifier = Modifier.fillMaxWidth().testTag("maker_prompt_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AuraCyanBright,
                            unfocusedBorderColor = AuraCardBorder,
                            focusedTextColor = AuraTextPrimary,
                            unfocusedTextColor = AuraTextPrimary
                        ),
                        shape = RoundedCornerShape(10.dp),
                        minLines = 2
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            viewModel.generateNewCreation(selectedType, makerPrompt)
                        },
                        enabled = !isGenerating,
                        colors = ButtonDefaults.buttonColors(containerColor = AuraCyanBright),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().testTag("generate_creation_button")
                    ) {
                        if (isGenerating) {
                            CircularProgressIndicator(color = AuraBackgroundDark, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Architecting Code & Logic...", color = AuraBackgroundDark, fontWeight = FontWeight.Bold)
                        } else {
                            Icon(Icons.Default.AutoFixHigh, contentDescription = null, tint = AuraBackgroundDark)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Generate ${selectedType.name.replace("_", " ")} Now", color = AuraBackgroundDark, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Creations Library List
        item {
            Text(
                text = "YOUR GENERATED CREATIONS",
                style = MaterialTheme.typography.labelSmall,
                color = AuraTextMuted,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(creations) { creation ->
                    Surface(
                        onClick = { viewModel.selectCreation(creation) },
                        color = if (selectedCreation?.id == creation.id) AuraSurfaceElevated else AuraSurfaceDark,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (selectedCreation?.id == creation.id) AuraCyan else AuraCardBorder
                        ),
                        modifier = Modifier.testTag("creation_item_${creation.id}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = when (creation.type) {
                                    CreationType.WEBSITE -> Icons.Default.Web
                                    CreationType.ANDROID_APP -> Icons.Default.PhoneAndroid
                                    CreationType.AI_AGENT -> Icons.Default.Psychology
                                },
                                contentDescription = null,
                                tint = AuraCyanBright,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = creation.title,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AuraTextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = creation.type.name,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = AuraTextMuted
                                )
                            }
                        }
                    }
                }
            }
        }

        // Active Creation Live Preview / Code Inspector
        if (selectedCreation != null) {
            val creation = selectedCreation!!
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("creation_preview_card"),
                    colors = CardDefaults.cardColors(containerColor = AuraSurfaceDark),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AuraCyan.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = creation.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = AuraTextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = creation.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AuraTextSecondary
                                )
                            }

                            Row {
                                FilterChip(
                                    selected = !showRawCode,
                                    onClick = { showRawCode = false },
                                    label = { Text("Preview", style = MaterialTheme.typography.labelSmall) }
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                FilterChip(
                                    selected = showRawCode,
                                    onClick = { showRawCode = true },
                                    label = { Text("Code", style = MaterialTheme.typography.labelSmall) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        if (showRawCode) {
                            Surface(
                                color = AuraBackgroundDark,
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, AuraCardBorder),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = when (creation.type) {
                                        CreationType.WEBSITE -> creation.htmlContent
                                        CreationType.ANDROID_APP -> creation.codeContent
                                        CreationType.AI_AGENT -> creation.systemPrompt
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    fontFamily = FontFamily.Monospace,
                                    color = AuraCyanBright,
                                    modifier = Modifier.padding(14.dp)
                                )
                            }
                        } else {
                            if (creation.type == CreationType.WEBSITE && creation.htmlContent.isNotBlank()) {
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(280.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .border(1.dp, AuraCardBorder, RoundedCornerShape(10.dp))
                                ) {
                                    AndroidView(
                                        factory = { ctx ->
                                            WebView(ctx).apply {
                                                webViewClient = WebViewClient()
                                                settings.javaScriptEnabled = true
                                                loadDataWithBaseURL(null, creation.htmlContent, "text/html", "UTF-8", null)
                                            }
                                        },
                                        update = { webView ->
                                            webView.loadDataWithBaseURL(null, creation.htmlContent, "text/html", "UTF-8", null)
                                        }
                                    )
                                }
                            } else if (creation.type == CreationType.AI_AGENT) {
                                Surface(
                                    color = AuraSurfaceElevated,
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Text(
                                            text = "AUTONOMOUS SUB-AGENT RUNTIME READY",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = AuraEmerald,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = creation.systemPrompt,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = AuraTextPrimary
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Button(
                                            onClick = {
                                                viewModel.processVoiceCommand("Test sub-agent '${creation.title}' with a sample analysis directive")
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = AuraIndigo),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("Dispatch Test Directive to Sub-Agent", style = MaterialTheme.typography.bodySmall)
                                        }
                                    }
                                }
                            } else {
                                Surface(
                                    color = AuraSurfaceElevated,
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Text(
                                            text = "MOBILE COMPOSE BLUEPRINT",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = AuraCyanBright,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = creation.codeContent.ifBlank { "Composable task orchestrator with Android architecture state engine." },
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

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
