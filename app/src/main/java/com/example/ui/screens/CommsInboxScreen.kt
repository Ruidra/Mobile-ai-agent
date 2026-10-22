package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.model.EmailCategory
import com.example.model.EmailItem
import com.example.ui.AuraViewModel
import com.example.ui.theme.*

@Composable
fun CommsInboxScreen(
    viewModel: AuraViewModel,
    modifier: Modifier = Modifier
) {
    val emails by viewModel.emails.collectAsState()
    val selectedEmail by viewModel.selectedEmail.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryFilter by remember { mutableStateOf<EmailCategory?>(null) }
    var customReplyText by remember { mutableStateOf("") }

    val filteredEmails = emails.filter { email ->
        val matchesCategory = selectedCategoryFilter == null || email.category == selectedCategoryFilter
        val matchesSearch = searchQuery.isBlank() ||
                email.subject.contains(searchQuery, ignoreCase = true) ||
                email.sender.contains(searchQuery, ignoreCase = true) ||
                email.snippet.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
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
                        text = "COMMUNICATIONS & INBOX",
                        style = MaterialTheme.typography.labelSmall,
                        color = AuraCyan,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Autonomous Triage",
                        style = MaterialTheme.typography.titleLarge,
                        color = AuraTextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Surface(
                    color = AuraSurfaceElevated,
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AuraCardBorder)
                ) {
                    Text(
                        text = "${emails.count { it.isUrgent }} Urgent Actions",
                        style = MaterialTheme.typography.labelSmall,
                        color = AuraAmber,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Search Bar across comms & files
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search emails, past chats & files...", color = AuraTextMuted) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = AuraCyanBright) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear", tint = AuraTextMuted)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("inbox_search_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AuraCyanBright,
                    unfocusedBorderColor = AuraCardBorder,
                    focusedTextColor = AuraTextPrimary,
                    unfocusedTextColor = AuraTextPrimary,
                    cursorColor = AuraCyanBright
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }

        // Category Filter Chips
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedCategoryFilter == null,
                    onClick = { selectedCategoryFilter = null },
                    label = { Text("All (${emails.size})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AuraCyanBright,
                        selectedLabelColor = AuraBackgroundDark
                    )
                )

                FilterChip(
                    selected = selectedCategoryFilter == EmailCategory.URGENT_ACTION,
                    onClick = { selectedCategoryFilter = EmailCategory.URGENT_ACTION },
                    label = { Text("Urgent Action") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AuraAmber,
                        selectedLabelColor = AuraBackgroundDark
                    )
                )

                FilterChip(
                    selected = selectedCategoryFilter == EmailCategory.NEEDS_REVIEW,
                    onClick = { selectedCategoryFilter = EmailCategory.NEEDS_REVIEW },
                    label = { Text("Needs Review") }
                )
            }
        }

        // Detailed View of Selected Email (if present)
        if (selectedEmail != null) {
            val email = selectedEmail!!
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("email_detail_card"),
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
                            Surface(
                                color = if (email.isUrgent) AuraRoseUrgent.copy(alpha = 0.15f) else AuraCyan.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = if (email.isUrgent) "URGENT PRIORITY" else "INBOX ITEM",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (email.isUrgent) AuraRoseUrgent else AuraCyan,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Row {
                                IconButton(
                                    onClick = { viewModel.toggleStarEmail(email.id) },
                                    modifier = Modifier.size(32.dp).testTag("star_email_button")
                                ) {
                                    Icon(
                                        imageVector = if (email.isStarred) Icons.Default.Star else Icons.Default.StarBorder,
                                        contentDescription = "Star",
                                        tint = if (email.isStarred) AuraAmber else AuraTextMuted
                                    )
                                }
                                IconButton(
                                    onClick = { viewModel.deleteEmail(email.id) },
                                    modifier = Modifier.size(32.dp).testTag("delete_email_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Archive",
                                        tint = AuraTextMuted
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = email.subject,
                            style = MaterialTheme.typography.titleMedium,
                            color = AuraTextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "From: ${email.sender} <${email.senderEmail}> • ${email.timeAgo}",
                            style = MaterialTheme.typography.bodySmall,
                            color = AuraTextSecondary
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = email.body,
                            style = MaterialTheme.typography.bodyMedium,
                            color = AuraTextPrimary,
                            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
                        )

                        // Attachments
                        if (email.hasAttachments) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "ATTACHMENTS",
                                style = MaterialTheme.typography.labelSmall,
                                color = AuraTextMuted,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                email.attachmentNames.forEach { file ->
                                    Surface(
                                        color = AuraSurfaceElevated,
                                        shape = RoundedCornerShape(8.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, AuraCardBorder)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(Icons.Default.AttachFile, contentDescription = null, tint = AuraCyan, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(text = file, style = MaterialTheme.typography.bodySmall, color = AuraTextPrimary)
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = AuraCardBorder)
                        Spacer(modifier = Modifier.height(12.dp))

                        // AI Smart Replies
                        Text(
                            text = "AI SMART REPLIES (${userProfile.tone.title.uppercase()})",
                            style = MaterialTheme.typography.labelSmall,
                            color = AuraCyanBright,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            email.suggestedReplies.forEach { reply ->
                                Surface(
                                    onClick = {
                                        viewModel.dispatchSmartReply(email, reply)
                                    },
                                    color = AuraSurfaceElevated,
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.Reply, contentDescription = null, tint = AuraCyan, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = reply,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = AuraTextPrimary,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Icon(Icons.Default.Send, contentDescription = null, tint = AuraTextMuted, modifier = Modifier.size(14.dp))
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Custom Reply Field
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = customReplyText,
                                onValueChange = { customReplyText = it },
                                placeholder = { Text("Draft custom reply in my tone...", color = AuraTextMuted, style = MaterialTheme.typography.bodySmall) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("custom_reply_input"),
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
                                onClick = {
                                    if (customReplyText.isNotBlank()) {
                                        viewModel.dispatchSmartReply(email, customReplyText)
                                        customReplyText = ""
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = AuraCyanBright),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.testTag("send_custom_reply_button")
                            ) {
                                Icon(Icons.Default.Send, contentDescription = "Send", tint = AuraBackgroundDark)
                            }
                        }
                    }
                }
            }
        }

        // Email List Header
        item {
            Text(
                text = "INBOX FEED",
                style = MaterialTheme.typography.labelSmall,
                color = AuraTextMuted,
                fontWeight = FontWeight.Bold
            )
        }

        // Email Items
        items(filteredEmails) { email ->
            Card(
                onClick = { viewModel.selectEmail(email) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("email_item_${email.id}"),
                colors = CardDefaults.cardColors(
                    containerColor = if (selectedEmail?.id == email.id) AuraSurfaceElevated else AuraSurfaceDark
                ),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (selectedEmail?.id == email.id) AuraCyan else AuraCardBorder
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(if (email.isUrgent) AuraRoseUrgent.copy(alpha = 0.2f) else AuraIndigo.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = email.sender.take(1),
                            style = MaterialTheme.typography.titleMedium,
                            color = if (email.isUrgent) AuraRoseUrgent else AuraIndigo,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = email.sender,
                                style = MaterialTheme.typography.titleMedium,
                                color = AuraTextPrimary,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                            Text(
                                text = email.timeAgo,
                                style = MaterialTheme.typography.labelSmall,
                                color = AuraTextMuted
                            )
                        }

                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = email.subject,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (email.isUrgent) AuraAmber else AuraCyanBright,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = email.snippet,
                            style = MaterialTheme.typography.bodySmall,
                            color = AuraTextSecondary,
                            maxLines = 2
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
