package com.example.model

enum class ScheduleCategory {
    MEETING,
    FOCUS_WORK,
    HEALTH_BREAK,
    PRIVATE_EVENT,
    WHATSAPP_CALL
}

data class ScheduleEvent(
    val id: String,
    val title: String,
    val timeRange: String,
    val startHour: Int,
    val startMinute: Int,
    val durationMinutes: Int,
    val isPrivate: Boolean = false,
    val isUrgent: Boolean = false,
    val category: ScheduleCategory = ScheduleCategory.MEETING,
    val prepNotes: String = "",
    val relatedFiles: List<String> = emptyList(),
    val isCompleted: Boolean = false,
    val attendees: List<String> = emptyList(),
    val location: String = "Google Meet / Remote"
)

enum class EmailCategory {
    URGENT_ACTION,
    NEEDS_REVIEW,
    INFO_NEWSLETTER
}

data class EmailItem(
    val id: String,
    val sender: String,
    val senderEmail: String,
    val subject: String,
    val snippet: String,
    val body: String,
    val timeAgo: String,
    val isUrgent: Boolean,
    val category: EmailCategory,
    val isRead: Boolean = false,
    val isStarred: Boolean = false,
    val suggestedReplies: List<String> = emptyList(),
    val hasAttachments: Boolean = false,
    val attachmentNames: List<String> = emptyList()
)

data class ContextFile(
    val id: String,
    val name: String,
    val type: String,
    val size: String,
    val summary: String,
    val content: String,
    val tags: List<String>,
    val lastModified: String
)

data class MarketQuote(
    val symbol: String,
    val name: String,
    val price: String,
    val change: String,
    val isPositive: Boolean,
    val category: String
)

data class NewsItem(
    val id: String,
    val headline: String,
    val source: String,
    val timeAgo: String,
    val category: String,
    val aiSummary: String
)

data class SocialTrend(
    val topic: String,
    val volume: String,
    val sentiment: String,
    val summary: String
)

enum class CreationType {
    WEBSITE,
    ANDROID_APP,
    AI_AGENT
}

data class GeneratedCreation(
    val id: String,
    val title: String,
    val type: CreationType,
    val description: String,
    val htmlContent: String = "",
    val codeContent: String = "",
    val systemPrompt: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

enum class ExecutiveTone(val title: String, val promptInstruction: String) {
    DIRECT("Direct & Decisive", "Deliver concise, razor-sharp executive summaries and action items. No fluff."),
    WARM_EXECUTIVE("Warm & Professional", "Polite, empathetic, executive diplomatic tone suitable for high-stake client relationships."),
    ANALYTICAL("Analytical & Deep", "Structured, data-driven, thorough with breakdown of risks, contingencies, and metrics."),
    ULTRA_CONCISE("Ultra-Brief / Telegram", "Extremely short bullet points, under 3 sentences.")
}

data class UserProfile(
    val name: String = "Dr. M. R. Biswas",
    val role: String = "Chief Executive & Researcher",
    val tone: ExecutiveTone = ExecutiveTone.DIRECT,
    val vipContacts: List<String> = listOf("Sarah Lin (CFO)", "David Chen (CTO)", "Marcus Vance (Investor)", "Family"),
    val autoSignoff: String = "Sent by Aura AI Executive Companion on behalf of Dr. Biswas",
    val backgroundMonitoring: Boolean = true,
    val lockScreenAlerts: Boolean = true,
    val voicePitch: Float = 1.0f,
    val voiceRate: Float = 1.05f
)

data class VoiceCommandState(
    val isListening: Boolean = false,
    val transcript: String = "",
    val responseText: String = "",
    val actionTaken: String? = null,
    val isProcessing: Boolean = false
)

data class StudyFlashcard(
    val id: String,
    val question: String,
    val answer: String,
    val topic: String
)
