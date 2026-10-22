package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AuraRepository
import com.example.model.*
import com.example.service.GeminiService
import com.example.service.MobileAutomationManager
import com.example.service.VoiceSpeakerManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class AuraTab(val label: String) {
    DASHBOARD("Core"),
    INBOX("Inbox"),
    AUTOMATION("Control"),
    INTEL("Intel"),
    STUDY("Study"),
    MAKER("Maker"),
    SETTINGS("Profile")
}

class AuraViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuraRepository()
    private val geminiService = GeminiService()
    private val automationManager = MobileAutomationManager(application)
    private val voiceSpeaker = VoiceSpeakerManager(application)

    // UI State Flows
    private val _currentTab = MutableStateFlow(AuraTab.DASHBOARD)
    val currentTab: StateFlow<AuraTab> = _currentTab.asStateFlow()

    val scheduleEvents: StateFlow<List<ScheduleEvent>> = repository.scheduleEvents
    val emails: StateFlow<List<EmailItem>> = repository.emails
    val contextFiles: StateFlow<List<ContextFile>> = repository.contextFiles
    val marketQuotes: StateFlow<List<MarketQuote>> = repository.marketQuotes
    val newsItems: StateFlow<List<NewsItem>> = repository.newsItems
    val socialTrends: StateFlow<List<SocialTrend>> = repository.socialTrends
    val creations: StateFlow<List<GeneratedCreation>> = repository.creations
    val userProfile: StateFlow<UserProfile> = repository.userProfile
    val flashcards: StateFlow<List<StudyFlashcard>> = repository.flashcards

    val isSpeaking: StateFlow<Boolean> = voiceSpeaker.isSpeaking

    private val _voiceState = MutableStateFlow(VoiceCommandState())
    val voiceState: StateFlow<VoiceCommandState> = _voiceState.asStateFlow()

    private val _selectedEmail = MutableStateFlow<EmailItem?>(null)
    val selectedEmail: StateFlow<EmailItem?> = _selectedEmail.asStateFlow()

    private val _webSearchQuery = MutableStateFlow("")
    val webSearchQuery: StateFlow<String> = _webSearchQuery.asStateFlow()

    private val _webSearchResults = MutableStateFlow<String?>(null)
    val webSearchResults: StateFlow<String?> = _webSearchResults.asStateFlow()

    private val _isSearchingWeb = MutableStateFlow(false)
    val isSearchingWeb: StateFlow<Boolean> = _isSearchingWeb.asStateFlow()

    private val _selectedCreation = MutableStateFlow<GeneratedCreation?>(null)
    val selectedCreation: StateFlow<GeneratedCreation?> = _selectedCreation.asStateFlow()

    private val _makerPrompt = MutableStateFlow("")
    val makerPrompt: StateFlow<String> = _makerPrompt.asStateFlow()

    private val _isGeneratingCreation = MutableStateFlow(false)
    val isGeneratingCreation: StateFlow<Boolean> = _isGeneratingCreation.asStateFlow()

    private val _statusBanner = MutableStateFlow<String?>("Aura Autonomous Companion Active • Zero Cognitive Overhead")
    val statusBanner: StateFlow<String?> = _statusBanner.asStateFlow()

    init {
        // Set default selected email
        _selectedEmail.value = repository.emails.value.firstOrNull()
        _selectedCreation.value = repository.creations.value.firstOrNull()
    }

    fun selectTab(tab: AuraTab) {
        _currentTab.value = tab
    }

    fun setStatusBanner(msg: String?) {
        _statusBanner.value = msg
    }

    // Schedule actions
    fun optimizeSchedule() {
        viewModelScope.launch {
            val result = repository.optimizeSchedule()
            _statusBanner.value = result
            voiceSpeaker.speak("Your day has been optimized. 15-minute buffers inserted, and urgent items flagged.")
        }
    }

    fun toggleScheduleEvent(id: String) {
        repository.toggleEventCompleted(id)
    }

    // Email actions
    fun selectEmail(email: EmailItem) {
        _selectedEmail.value = email
        repository.markEmailRead(email.id)
    }

    fun toggleStarEmail(id: String) {
        repository.toggleEmailStarred(id)
    }

    fun deleteEmail(id: String) {
        repository.deleteEmail(id)
        if (_selectedEmail.value?.id == id) {
            _selectedEmail.value = repository.emails.value.firstOrNull()
        }
    }

    fun dispatchSmartReply(email: EmailItem, replyText: String) {
        val fullBody = "$replyText\n\n--\n${userProfile.value.autoSignoff}"
        automationManager.openEmailDraft(email.senderEmail, "Re: ${email.subject}", fullBody)
        _statusBanner.value = "Smart reply prepared for ${email.sender}"
    }

    // WhatsApp & Mobile Automation
    fun triggerWhatsAppCall(contact: String, isVideo: Boolean) {
        val feedback = automationManager.startWhatsAppCall(contact, isVideo)
        _statusBanner.value = feedback
        voiceSpeaker.speak(feedback)
    }

    fun triggerWhatsAppMessage(contact: String, message: String) {
        val feedback = automationManager.sendWhatsAppMessage(contact, message)
        _statusBanner.value = feedback
    }

    fun openApp(actionName: String) {
        val result = when (actionName) {
            "CAMERA" -> automationManager.openCamera()
            "SETTINGS" -> automationManager.openSettings()
            "ALARMS" -> automationManager.openAlarms()
            "BROWSER" -> automationManager.openWebSearch("Global Tech & AI News")
            else -> "Action executed."
        }
        _statusBanner.value = result
    }

    fun sendLockScreenUrgentNotification() {
        val result = automationManager.postUrgentLockScreenAlert(
            "⚠️ URGENT: Board Strategy Session in 25m",
            "Prep ready: Marcus Vance legal clause 4.2 review linked from CFO email."
        )
        _statusBanner.value = result
        voiceSpeaker.speak("High priority alert dispatched to your lock screen and notification panel.")
    }

    // Web Search
    fun updateSearchQuery(query: String) {
        _webSearchQuery.value = query
    }

    fun executeWebSearch(query: String) {
        if (query.isBlank()) return
        _isSearchingWeb.value = true
        _webSearchResults.value = null

        viewModelScope.launch {
            val contextData = "Current Date: Oct 2026. Relevant Files: " +
                    repository.contextFiles.value.joinToString { it.name }
            val prompt = "Provide a comprehensive, authoritative executive search synthesis and analysis for: '$query'. Include key facts, verified sources, and strategic takeaways."

            val result = geminiService.generateExecutiveResponse(
                userPrompt = prompt,
                profile = userProfile.value,
                contextSummary = contextData
            )

            _webSearchResults.value = result
            _isSearchingWeb.value = false
            _statusBanner.value = "Search synthesized for '$query'"
        }
    }

    // Maker Studio - Create Web / App / Agent
    fun setMakerPrompt(prompt: String) {
        _makerPrompt.value = prompt
    }

    fun selectCreation(creation: GeneratedCreation) {
        _selectedCreation.value = creation
    }

    fun generateNewCreation(type: CreationType, userInstructions: String) {
        val instructions = if (userInstructions.isNotBlank()) userInstructions else when (type) {
            CreationType.WEBSITE -> "Create a sleek modern dark-mode executive dashboard with animated metrics and clean glass cards."
            CreationType.ANDROID_APP -> "Scaffold an autonomous task delegator with background service worker and real-time state machine."
            CreationType.AI_AGENT -> "Configure an autonomous legal and risk sentinel agent that monitors contracts for ambiguous liability clauses."
        }

        _isGeneratingCreation.value = true

        viewModelScope.launch {
            val prompt = "Generate a complete, fully functional ${type.name} based on: $instructions. If website, provide complete valid HTML with embedded CSS styling. If agent, provide system architecture and persona prompts. If app, provide Kotlin architecture."

            val response = geminiService.generateExecutiveResponse(
                userPrompt = prompt,
                profile = userProfile.value,
                contextSummary = "User: ${userProfile.value.name}, ${userProfile.value.role}"
            )

            val newCreation = when (type) {
                CreationType.WEBSITE -> {
                    val cleanHtml = if (response.contains("<html", ignoreCase = true)) {
                        response.substringAfter("```html").substringBefore("```").ifEmpty { response }
                    } else {
                        """
                        <!DOCTYPE html>
                        <html>
                        <head><style>body{background:#0F172A;color:#F8FAFC;font-family:sans-serif;padding:24px;}.card{background:#1E293B;padding:20px;border-radius:12px;margin-top:16px;border:1px solid #334155;}h1{color:#22D3EE;}</style></head>
                        <body>
                            <h1>Generated Portal: $instructions</h1>
                            <div class="card"><p>$response</p></div>
                        </body>
                        </html>
                        """.trimIndent()
                    }
                    GeneratedCreation(
                        id = "cr_" + System.currentTimeMillis(),
                        title = instructions.take(30),
                        type = type,
                        description = "Autonomous web generator output for '$instructions'",
                        htmlContent = cleanHtml
                    )
                }
                CreationType.AI_AGENT -> {
                    GeneratedCreation(
                        id = "cr_" + System.currentTimeMillis(),
                        title = instructions.take(30),
                        type = type,
                        description = "Autonomous sub-agent instantiated for '$instructions'",
                        systemPrompt = response
                    )
                }
                CreationType.ANDROID_APP -> {
                    GeneratedCreation(
                        id = "cr_" + System.currentTimeMillis(),
                        title = instructions.take(30),
                        type = type,
                        description = "Android Compose Architecture generated for '$instructions'",
                        codeContent = response
                    )
                }
            }

            repository.addCreation(newCreation)
            _selectedCreation.value = newCreation
            _isGeneratingCreation.value = false
            _statusBanner.value = "${type.name} created successfully."
            voiceSpeaker.speak("I have generated your ${type.name} project. You can inspect the code and preview.")
        }
    }

    // Voice Command Central Handler
    fun processVoiceCommand(transcript: String) {
        if (transcript.isBlank()) return

        _voiceState.value = VoiceCommandState(
            isListening = false,
            transcript = transcript,
            isProcessing = true
        )

        viewModelScope.launch {
            val contextData = """
                Schedule: ${repository.scheduleEvents.value.joinToString { "${it.title} at ${it.timeRange}" }}
                Urgent Emails: ${repository.emails.value.filter { it.isUrgent }.joinToString { "${it.subject} from ${it.sender}" }}
                User VIPs: ${userProfile.value.vipContacts.joinToString()}
            """.trimIndent()

            val aiResponse = geminiService.generateExecutiveResponse(
                userPrompt = transcript,
                profile = userProfile.value,
                contextSummary = contextData
            )

            // Parse physical action execution
            val lower = transcript.lowercase()
            var actionTaken: String? = null

            if (lower.contains("whatsapp") && (lower.contains("call") || lower.contains("video"))) {
                val isVideo = lower.contains("video")
                val contact = if (lower.contains("elena") || lower.contains("team") || lower.contains("lead")) "Elena (Engineering)" else "Sarah Lin"
                actionTaken = automationManager.startWhatsAppCall(contact, isVideo)
            } else if (lower.contains("optimize") && (lower.contains("schedule") || lower.contains("day") || lower.contains("calendar"))) {
                actionTaken = repository.optimizeSchedule()
                _currentTab.value = AuraTab.DASHBOARD
            } else if (lower.contains("inbox") || lower.contains("email") || lower.contains("mail")) {
                _currentTab.value = AuraTab.INBOX
                actionTaken = "Switched to Comms Hub. Highlighting Marcus Vance term sheet."
            } else if (lower.contains("search") || lower.contains("google") || lower.contains("web")) {
                val query = transcript.substringAfter("search").substringAfter("for").trim()
                if (query.isNotEmpty()) {
                    executeWebSearch(query)
                    _currentTab.value = AuraTab.INTEL
                    actionTaken = "Initiated live web intelligence for '$query'."
                }
            } else if (lower.contains("study") || lower.contains("read")) {
                _currentTab.value = AuraTab.STUDY
                actionTaken = "Opened Study & Reading Vault."
            } else if (lower.contains("make") || lower.contains("create") || lower.contains("build")) {
                _currentTab.value = AuraTab.MAKER
                actionTaken = "Opened Maker Studio to scaffold your project."
            } else if (lower.contains("camera")) {
                actionTaken = automationManager.openCamera()
            } else if (lower.contains("alarm") || lower.contains("clock")) {
                actionTaken = automationManager.openAlarms()
            }

            _voiceState.value = VoiceCommandState(
                isListening = false,
                transcript = transcript,
                responseText = aiResponse,
                actionTaken = actionTaken,
                isProcessing = false
            )

            voiceSpeaker.speak(aiResponse, userProfile.value.voicePitch, userProfile.value.voiceRate)
        }
    }

    fun dismissVoiceDialog() {
        voiceSpeaker.stop()
        _voiceState.value = VoiceCommandState()
    }

    fun speakText(text: String) {
        voiceSpeaker.speak(text, userProfile.value.voicePitch, userProfile.value.voiceRate)
    }

    fun stopSpeaking() {
        voiceSpeaker.stop()
    }

    // Profile Settings
    fun updateTone(tone: ExecutiveTone) {
        repository.updateTone(tone)
    }

    fun toggleBackgroundMonitoring(enabled: Boolean) {
        repository.toggleBackgroundMonitoring(enabled)
    }

    fun toggleLockScreenAlerts(enabled: Boolean) {
        repository.toggleLockScreenAlerts(enabled)
    }

    fun updateVoiceTuning(pitch: Float, rate: Float) {
        repository.updateVoiceSettings(pitch, rate)
    }

    override fun onCleared() {
        super.onCleared()
        voiceSpeaker.destroy()
    }
}
