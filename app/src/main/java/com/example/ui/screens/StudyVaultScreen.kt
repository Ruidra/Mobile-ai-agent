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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.ContextFile
import com.example.model.StudyFlashcard
import com.example.ui.AuraViewModel
import com.example.ui.theme.*

@Composable
fun StudyVaultScreen(
    viewModel: AuraViewModel,
    modifier: Modifier = Modifier
) {
    val contextFiles by viewModel.contextFiles.collectAsState()
    val flashcards by viewModel.flashcards.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedFile by remember { mutableStateOf<ContextFile?>(contextFiles.firstOrNull()) }
    var revealedFlashcardId by remember { mutableStateOf<String?>(null) }

    val filteredFiles = contextFiles.filter { file ->
        searchQuery.isBlank() ||
                file.name.contains(searchQuery, ignoreCase = true) ||
                file.summary.contains(searchQuery, ignoreCase = true) ||
                file.tags.any { it.contains(searchQuery, ignoreCase = true) }
    }

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
                        text = "DOCUMENT VAULT & COGNITIVE STUDY",
                        style = MaterialTheme.typography.labelSmall,
                        color = AuraCyan,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Files & Accelerated Learning",
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
                        text = "${contextFiles.size} Indexed Docs",
                        style = MaterialTheme.typography.labelSmall,
                        color = AuraIndigo,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search files, past papers, notes & tags...", color = AuraTextMuted) },
                leadingIcon = { Icon(Icons.Default.FolderOpen, contentDescription = null, tint = AuraCyanBright) },
                modifier = Modifier.fillMaxWidth().testTag("files_vault_search_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AuraCyanBright,
                    unfocusedBorderColor = AuraCardBorder,
                    focusedTextColor = AuraTextPrimary,
                    unfocusedTextColor = AuraTextPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }

        // Active Study Focus Card
        if (selectedFile != null) {
            val file = selectedFile!!
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("active_study_card"),
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
                            Surface(
                                color = AuraCyan.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "STUDY FOCUS • ${file.type}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = AuraCyanBright,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = {
                                        if (isSpeaking) viewModel.stopSpeaking()
                                        else viewModel.speakText("${file.name}. ${file.summary}. ${file.content}")
                                    },
                                    modifier = Modifier.testTag("read_study_doc_button")
                                ) {
                                    Icon(
                                        imageVector = if (isSpeaking) Icons.Default.Stop else Icons.Default.VolumeUp,
                                        contentDescription = "Read Aloud",
                                        tint = if (isSpeaking) AuraRoseUrgent else AuraCyanBright
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = file.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = AuraTextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = file.summary,
                            style = MaterialTheme.typography.bodyMedium,
                            color = AuraTextSecondary
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            color = AuraBackgroundDark,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, AuraCardBorder)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "EXCERPT & CONTEXTUAL CONTENT",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = AuraTextMuted,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = file.content,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AuraTextPrimary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = {
                                viewModel.processVoiceCommand("Help me study and extract deep insights from ${file.name}")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AuraCyanBright),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth().testTag("ai_study_insights_btn")
                        ) {
                            Icon(Icons.Default.School, contentDescription = null, tint = AuraBackgroundDark)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Generate Active Study Session", color = AuraBackgroundDark, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Active Recall Flashcards Section
        item {
            Text(
                text = "ACTIVE RECALL FLASHCARDS",
                style = MaterialTheme.typography.labelSmall,
                color = AuraTextMuted,
                fontWeight = FontWeight.Bold
            )
        }

        items(flashcards) { card ->
            val isRevealed = revealedFlashcardId == card.id
            Card(
                onClick = {
                    revealedFlashcardId = if (isRevealed) null else card.id
                },
                modifier = Modifier.fillMaxWidth().testTag("flashcard_${card.id}"),
                colors = CardDefaults.cardColors(
                    containerColor = if (isRevealed) AuraSurfaceElevated else AuraSurfaceDark
                ),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isRevealed) AuraIndigo else AuraCardBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(
                            color = AuraIndigo.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = card.topic,
                                style = MaterialTheme.typography.labelSmall,
                                color = AuraIndigo,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = if (isRevealed) "Hide Answer" else "Tap to Reveal",
                            style = MaterialTheme.typography.labelSmall,
                            color = AuraCyan
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Q: ${card.question}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AuraTextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )

                    if (isRevealed) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "A: ${card.answer}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = AuraCyanBright
                        )
                    }
                }
            }
        }

        // All Indexed Files in Vault
        item {
            Text(
                text = "INDEXED FILES & COMMUNICATIONS",
                style = MaterialTheme.typography.labelSmall,
                color = AuraTextMuted,
                fontWeight = FontWeight.Bold
            )
        }

        items(filteredFiles) { file ->
            Card(
                onClick = { selectedFile = file },
                modifier = Modifier.fillMaxWidth().testTag("vault_file_${file.id}"),
                colors = CardDefaults.cardColors(
                    containerColor = if (selectedFile?.id == file.id) AuraSurfaceElevated else AuraSurfaceDark
                ),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (selectedFile?.id == file.id) AuraCyan else AuraCardBorder
                )
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = when (file.type) {
                            "PDF" -> Icons.Default.PictureAsPdf
                            "DOC" -> Icons.Default.Description
                            else -> Icons.Default.Article
                        },
                        contentDescription = null,
                        tint = AuraCyan,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = file.name,
                            style = MaterialTheme.typography.bodyMedium,
                            color = AuraTextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "${file.size} • Modified ${file.lastModified}",
                            style = MaterialTheme.typography.bodySmall,
                            color = AuraTextMuted
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
