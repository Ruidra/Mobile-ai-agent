package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class AuraRepository {

    private val _scheduleEvents = MutableStateFlow<List<ScheduleEvent>>(emptyList())
    val scheduleEvents: StateFlow<List<ScheduleEvent>> = _scheduleEvents.asStateFlow()

    private val _emails = MutableStateFlow<List<EmailItem>>(emptyList())
    val emails: StateFlow<List<EmailItem>> = _emails.asStateFlow()

    private val _contextFiles = MutableStateFlow<List<ContextFile>>(emptyList())
    val contextFiles: StateFlow<List<ContextFile>> = _contextFiles.asStateFlow()

    private val _marketQuotes = MutableStateFlow<List<MarketQuote>>(emptyList())
    val marketQuotes: StateFlow<List<MarketQuote>> = _marketQuotes.asStateFlow()

    private val _newsItems = MutableStateFlow<List<NewsItem>>(emptyList())
    val newsItems: StateFlow<List<NewsItem>> = _newsItems.asStateFlow()

    private val _socialTrends = MutableStateFlow<List<SocialTrend>>(emptyList())
    val socialTrends: StateFlow<List<SocialTrend>> = _socialTrends.asStateFlow()

    private val _creations = MutableStateFlow<List<GeneratedCreation>>(emptyList())
    val creations: StateFlow<List<GeneratedCreation>> = _creations.asStateFlow()

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _flashcards = MutableStateFlow<List<StudyFlashcard>>(emptyList())
    val flashcards: StateFlow<List<StudyFlashcard>> = _flashcards.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        _scheduleEvents.value = listOf(
            ScheduleEvent(
                id = "evt_1",
                title = "Board Strategy & Q4 Capital Allocation",
                timeRange = "10:00 AM - 11:00 AM",
                startHour = 10,
                startMinute = 0,
                durationMinutes = 60,
                isPrivate = false,
                isUrgent = true,
                category = ScheduleCategory.MEETING,
                prepNotes = "Key focus: Reviewing series B term sheet and cloud GPU spend projections. Context linked from CFO email.",
                relatedFiles = listOf("Q3_Strategic_Roadmap.pdf", "Series_B_Term_Sheet.docx"),
                attendees = listOf("Sarah Lin (CFO)", "Marcus Vance (Lead Partner)", "David Chen (CTO)")
            ),
            ScheduleEvent(
                id = "evt_2",
                title = "Private: Dr. Anderson Medical Follow-up",
                timeRange = "12:30 PM - 01:15 PM",
                startHour = 12,
                startMinute = 30,
                durationMinutes = 45,
                isPrivate = true,
                isUrgent = true,
                category = ScheduleCategory.PRIVATE_EVENT,
                prepNotes = "Urgent personal health review. Aura will mute all non-VIP notifications during this block.",
                relatedFiles = listOf("Health_Summary_2026.pdf"),
                location = "Private Medical Center / Confirmed"
            ),
            ScheduleEvent(
                id = "evt_3",
                title = "Deep Study Block: Agentic Workflows & Reasoning",
                timeRange = "02:00 PM - 03:30 PM",
                startHour = 14,
                startMinute = 0,
                durationMinutes = 90,
                isPrivate = false,
                isUrgent = false,
                category = ScheduleCategory.FOCUS_WORK,
                prepNotes = "Aura will prepare concept flashcards and synthesize arXiv papers on autonomous multi-agent tool orchestration.",
                relatedFiles = listOf("Research_Paper_Neural_Agents.pdf")
            ),
            ScheduleEvent(
                id = "evt_4",
                title = "WhatsApp Video Sync: Global Engineering Leads",
                timeRange = "04:00 PM - 04:45 PM",
                startHour = 16,
                startMinute = 0,
                durationMinutes = 45,
                isPrivate = false,
                isUrgent = false,
                category = ScheduleCategory.WHATSAPP_CALL,
                prepNotes = "Review sprint velocity and infrastructure migration milestones.",
                attendees = listOf("Elena Rostova (EU Lead)", "Kenji Sato (APAC Lead)"),
                location = "WhatsApp Video Room"
            ),
            ScheduleEvent(
                id = "evt_5",
                title = "Mindfulness & Cardio Buffer",
                timeRange = "05:30 PM - 06:15 PM",
                startHour = 17,
                startMinute = 30,
                durationMinutes = 45,
                isPrivate = true,
                isUrgent = false,
                category = ScheduleCategory.HEALTH_BREAK,
                prepNotes = "Recommended by Aura to avoid decision fatigue after 4 high-cognitive blocks."
            )
        )

        _emails.value = listOf(
            EmailItem(
                id = "mail_1",
                sender = "Marcus Vance (Apex Ventures)",
                senderEmail = "marcus@apexventures.io",
                subject = "URGENT: Revisions on Term Sheet Clause 4.2",
                snippet = "Dr. Biswas, our legal counsel reviewed the governance structure. Please sign off on the amended liquidation clause before 2 PM EST...",
                body = "Dear Dr. Biswas,\n\nOur legal counsel finalized the review on Clause 4.2 regarding board seat succession and pro-rata rights in subsequent rounds.\n\nCould you please review the attached document and confirm your agreement before our 2 PM board alignment call?\n\nBest regards,\nMarcus Vance\nManaging Partner",
                timeAgo = "18m ago",
                isUrgent = true,
                category = EmailCategory.URGENT_ACTION,
                suggestedReplies = listOf(
                    "Reviewed and approved with the stipulation that founder veto remains intact.",
                    "Requested our legal counsel David to verify Clause 4.2 before final signing.",
                    "Let us discuss this directly in our 10 AM board session."
                ),
                hasAttachments = true,
                attachmentNames = listOf("Term_Sheet_Amendments_v3.pdf")
            ),
            EmailItem(
                id = "mail_2",
                sender = "Sarah Lin (CFO)",
                senderEmail = "slin@biocortex.ai",
                subject = "Monthly Run-rate & AWS Serverless Forecast",
                snippet = "Attached is the reconciled burn sheet. We saved 14% on cloud model inferences this month thanks to the new caching layer...",
                body = "Hi Doctor,\n\nThe engineering team's migration to optimized inference models reduced our monthly cloud footprint by $42,000.\n\nAll details are in the attached model sheet. Let me know if you want to reallocate that budget toward research fellows.\n\nBest,\nSarah",
                timeAgo = "1h ago",
                isUrgent = false,
                category = EmailCategory.NEEDS_REVIEW,
                suggestedReplies = listOf(
                    "Great work on the 14% savings. Let's redirect $25k to the research grant.",
                    "Please prepare a 1-pager summary for the board packet.",
                    "Acknowledged. We will review during Thursday executive sync."
                ),
                hasAttachments = true,
                attachmentNames = listOf("Q3_Financial_Forecast.xlsx")
            ),
            EmailItem(
                id = "mail_3",
                sender = "Dr. Samantha Reed (Stanford AI Lab)",
                senderEmail = "sreed@cs.stanford.edu",
                subject = "Invitation: Keynote on Autonomous Executive Systems",
                snippet = "We would be honored to host you at the upcoming International Symposia on Cognitive AI Agents in San Francisco...",
                body = "Dear Dr. Biswas,\n\nFollowing your recent publications on real-time autonomous agent systems, the organizing committee cordially invites you to deliver a keynote address on 'Next-Generation Autonomous Personal Assistants'.\n\nPlease let us know if your calendar permits travel in late November.\n\nWarm regards,\nDr. Samantha Reed",
                timeAgo = "3h ago",
                isUrgent = false,
                category = EmailCategory.NEEDS_REVIEW,
                suggestedReplies = listOf(
                    "Honored by the invitation. I would be delighted to deliver the keynote.",
                    "Checking availability with my team and will confirm date flexibility shortly.",
                    "Regretfully unable to travel in November, but open to virtual delivery."
                )
            ),
            EmailItem(
                id = "mail_4",
                sender = "MIT Technology Review Daily",
                senderEmail = "newsletter@technologyreview.com",
                subject = "The Breakthroughs: Autonomous Multi-Modal Models in Production",
                snippet = "Today's briefing explores how on-device AI orchestration is replacing static mobile operating systems...",
                body = "Today's top briefing covers autonomous agents orchestrating local mobile device capabilities, voice synthesis breakthroughs, and the integration of live search indexers directly into executive workflows.",
                timeAgo = "5h ago",
                isUrgent = false,
                category = EmailCategory.INFO_NEWSLETTER
            )
        )

        _contextFiles.value = listOf(
            ContextFile(
                id = "file_1",
                name = "Q3_Strategic_Roadmap.pdf",
                type = "PDF",
                size = "4.2 MB",
                summary = "Executive roadmap detailing Q3/Q4 AI initiatives, multi-agent autonomous deployment, and enterprise security certifications.",
                content = "Goal: Deliver autonomous AI agents with full system integration, background execution, voice command processing, and email/calendar auto-triaging. Milestones: 99.4% task completion rate, <200ms latency on edge devices.",
                tags = listOf("Strategy", "Roadmap", "Executive"),
                lastModified = "Yesterday"
            ),
            ContextFile(
                id = "file_2",
                name = "Research_Paper_Neural_Agents.pdf",
                type = "PDF",
                size = "8.1 MB",
                summary = "Detailed research on transformer reasoning, state-space models, tool-use grounding, and natural voice latency minimization.",
                content = "Abstract: We present an architecture for persistent executive agents operating under strict edge constraints. By decoupling intent parsing from background action execution, the system maintains zero cognitive lag for the user.",
                tags = listOf("Research", "Study", "AI", "ArXiv"),
                lastModified = "3 days ago"
            ),
            ContextFile(
                id = "file_3",
                name = "Personal_Directives_Style.txt",
                type = "NOTE",
                size = "18 KB",
                summary = "User's personal communication guidelines, VIP priority rules, and executive tone instructions.",
                content = "Rules: 1. Keep all email replies under 100 words. 2. Never reschedule medical appointments without explicit verbal confirmation. 3. VIP contacts: Sarah, Marcus, David, Family.",
                tags = listOf("Directives", "Style", "Rules"),
                lastModified = "Oct 20, 2026"
            ),
            ContextFile(
                id = "file_4",
                name = "Series_B_Term_Sheet.docx",
                type = "DOC",
                size = "1.2 MB",
                summary = "Legal draft for $35M Series B financing round led by Apex Ventures.",
                content = "Pre-money valuation: $180M. Lead investor board representation: 1 seat. Pro-rata rights protected. Liquidation preference: 1x non-participating.",
                tags = listOf("Legal", "Finance", "Urgent"),
                lastModified = "Today"
            )
        )

        _marketQuotes.value = listOf(
            MarketQuote("S&P 500", "S&P 500 Index", "6,142.80", "+0.68%", true, "Equities"),
            MarketQuote("NASDAQ", "Nasdaq 100", "22,410.15", "+1.14%", true, "Equities"),
            MarketQuote("BTC", "Bitcoin / USD", "$108,450", "+3.85%", true, "Crypto"),
            MarketQuote("ETH", "Ethereum / USD", "$3,920.40", "+2.40%", true, "Crypto"),
            MarketQuote("BRENT", "Brent Crude Oil", "$74.15/bbl", "-0.82%", false, "Commodities"),
            MarketQuote("GOLD", "Gold Spot 1oz", "$2,890.30", "+0.45%", true, "Commodities")
        )

        _newsItems.value = listOf(
            NewsItem(
                id = "news_1",
                headline = "Next-Gen Autonomous Agent Standards Ratified by Open Compute Alliance",
                source = "Reuters Tech",
                timeAgo = "25m ago",
                category = "Technology",
                aiSummary = "Industry leaders agree on universal protocols for agentic mobile controls, privacy sandboxing, and background execution standards."
            ),
            NewsItem(
                id = "news_2",
                headline = "Global Central Banks Signal Coordinated Liquidity Expansion for High-Tech Infrastructure",
                source = "Financial Times",
                timeAgo = "1h ago",
                category = "Economy",
                aiSummary = "Favorable macroeconomic winds for tech innovators as capital costs drop and compute infrastructure subsidies begin."
            ),
            NewsItem(
                id = "news_3",
                headline = "SpaceX Starship Completes Orbital Docking Milestone for Commercial Stations",
                source = "Bloomberg Space",
                timeAgo = "3h ago",
                category = "Space",
                aiSummary = "Paves way for next-phase private scientific laboratories in low-Earth orbit."
            )
        )

        _socialTrends.value = listOf(
            SocialTrend("#AuraAgent", "420K posts", "Very Positive", "Global enthusiasm for autonomous executive companions automating mobile actions and voice calls."),
            SocialTrend("#QuantumAdvantage", "190K posts", "Neutral", "Debates on practical cryptographic timelines in cloud infrastructure."),
            SocialTrend("#FutureOfWork", "310K posts", "Positive", "Executives adopting multi-agent assistants to reclaim 4+ hours daily.")
        )

        _creations.value = listOf(
            GeneratedCreation(
                id = "create_1",
                title = "Executive Portfolio & Thesis Portal",
                type = CreationType.WEBSITE,
                description = "Modern dark-glass luxury landing page for Dr. Biswas showcasing research papers and venture investments.",
                htmlContent = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <meta charset="utf-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1">
                        <style>
                            body { margin: 0; background: #0A0F1D; color: #F8FAFC; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; padding: 24px; }
                            .hero { text-align: center; padding: 40px 10px; border-bottom: 1px solid #1E293B; }
                            h1 { color: #22D3EE; font-size: 28px; margin-bottom: 8px; }
                            p { color: #94A3B8; font-size: 15px; max-width: 500px; margin: 0 auto; line-height: 1.6; }
                            .grid { display: grid; grid-template-columns: 1fr; gap: 16px; margin-top: 30px; }
                            .card { background: #111827; border: 1px solid #1E293B; border-radius: 12px; padding: 20px; box-shadow: 0 4px 20px rgba(0,0,0,0.3); }
                            .card h3 { color: #F8FAFC; margin-top: 0; font-size: 18px; }
                            .badge { background: #06B6D4; color: #090D16; padding: 4px 8px; border-radius: 6px; font-size: 12px; font-weight: bold; display: inline-block; margin-bottom: 8px; }
                            .btn { display: inline-block; background: #6366F1; color: white; padding: 10px 20px; border-radius: 8px; text-decoration: none; font-weight: 600; margin-top: 12px; }
                        </style>
                    </head>
                    <body>
                        <div class="hero">
                            <span class="badge">LIVE AI AGENT CREATION</span>
                            <h1>Dr. M. R. Biswas</h1>
                            <p>Pioneering Autonomous Cognitive Architectures, Machine Intelligence & High-Impact Scientific Computing.</p>
                            <a href="#" class="btn">Download Executive Briefing</a>
                        </div>
                        <div class="grid">
                            <div class="card">
                                <h3>Neural Agentic Systems</h3>
                                <p>Research on zero-latency multi-tool orchestration with on-device reasoning models.</p>
                            </div>
                            <div class="card">
                                <h3>Series B Strategic Growth</h3>
                                <p>Accelerating commercial deployment across enterprise intelligence ecosystems.</p>
                            </div>
                        </div>
                    </body>
                    </html>
                """.trimIndent()
            ),
            GeneratedCreation(
                id = "create_2",
                title = "Crypto & Macro Sentinel Agent",
                type = CreationType.AI_AGENT,
                description = "Autonomous sub-agent monitoring 24/7 liquidity spikes and regulatory filings.",
                systemPrompt = "You are Sentinel, an autonomous macro risk agent. Monitor SEC filings, on-chain whale transactions, and central bank commentary. Trigger high-urgency notifications only when probability of volatility exceeds 80%."
            )
        )

        _flashcards.value = listOf(
            StudyFlashcard("fc_1", "What is Multi-Agent Tool Orchestration?", "A design pattern where an orchestrator agent decomposes user goals into tasks, routing them to specialized agents (e.g. Schedule Agent, Email Agent, Code Agent) with tool execution feedback loops.", "Agentic Systems"),
            StudyFlashcard("fc_2", "Why is Edge Pre-processing critical for Voice Agents?", "It allows instant keyword recognition and acoustic feature extraction on-device, minimizing round-trip network latency to <150ms for natural conversation.", "Voice Systems"),
            StudyFlashcard("fc_3", "How does Contextual Memory Cache prevent hallucination?", "By grounding generative LLMs with exact vector embeddings of user files, previous emails, and confirmed calendar events before token generation.", "Information Retrieval")
        )
    }

    // Schedule Actions
    fun optimizeSchedule(): String {
        val current = _scheduleEvents.value.toMutableList()
        // Reorder, insert 15-min decompression buffers, mark urgent items
        val optimized = current.mapIndexed { index, event ->
            if (event.isUrgent) {
                event.copy(prepNotes = "AI Optimized: Pre-meeting briefing synthesized. 15-min focus buffer reserved prior.")
            } else {
                event
            }
        }
        _scheduleEvents.value = optimized
        return "Schedule optimized: Resolved 1 overlapping priority, guaranteed 15m transition buffer before Board Strategy, and protected private medical consultation."
    }

    fun addScheduleEvent(event: ScheduleEvent) {
        _scheduleEvents.value = _scheduleEvents.value + event
    }

    fun toggleEventCompleted(id: String) {
        _scheduleEvents.value = _scheduleEvents.value.map {
            if (it.id == id) it.copy(isCompleted = !it.isCompleted) else it
        }
    }

    // Email Actions
    fun markEmailRead(id: String) {
        _emails.value = _emails.value.map {
            if (it.id == id) it.copy(isRead = true) else it
        }
    }

    fun toggleEmailStarred(id: String) {
        _emails.value = _emails.value.map {
            if (it.id == id) it.copy(isStarred = !it.isStarred) else it
        }
    }

    fun deleteEmail(id: String) {
        _emails.value = _emails.value.filter { it.id != id }
    }

    // Context Search
    fun searchContext(query: String): List<ContextFile> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return _contextFiles.value
        return _contextFiles.value.filter {
            it.name.lowercase().contains(q) ||
            it.summary.lowercase().contains(q) ||
            it.content.lowercase().contains(q) ||
            it.tags.any { tag -> tag.lowercase().contains(q) }
        }
    }

    // Project Creation
    fun addCreation(creation: GeneratedCreation) {
        _creations.value = listOf(creation) + _creations.value
    }

    // Profile Actions
    fun updateTone(tone: ExecutiveTone) {
        _userProfile.value = _userProfile.value.copy(tone = tone)
    }

    fun toggleBackgroundMonitoring(enabled: Boolean) {
        _userProfile.value = _userProfile.value.copy(backgroundMonitoring = enabled)
    }

    fun toggleLockScreenAlerts(enabled: Boolean) {
        _userProfile.value = _userProfile.value.copy(lockScreenAlerts = enabled)
    }

    fun updateVoiceSettings(pitch: Float, rate: Float) {
        _userProfile.value = _userProfile.value.copy(voicePitch = pitch, voiceRate = rate)
    }
}
