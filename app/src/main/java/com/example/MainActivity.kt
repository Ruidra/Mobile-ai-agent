package com.example

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.AuraTab
import com.example.ui.AuraViewModel
import com.example.ui.components.VoiceCommandDialog
import com.example.ui.screens.*
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {

    private val viewModel: AuraViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme(darkTheme = true) {
                AuraMainApp(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuraMainApp(viewModel: AuraViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()
    val voiceState by viewModel.voiceState.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()

    var showVoiceDialog by remember { mutableStateOf(false) }

    // System Speech Recognizer Launcher
    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spokenText = result.data
                ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                ?.firstOrNull()
            if (!spokenText.isNullOrBlank()) {
                showVoiceDialog = true
                viewModel.processVoiceCommand(spokenText)
            }
        }
    }

    fun launchVoiceRecognition() {
        try {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak command to Aura...")
            }
            speechRecognizerLauncher.launch(intent)
        } catch (e: Exception) {
            // In emulator or environments without SpeechRecognizer service, open rich modal directly
            showVoiceDialog = true
        }
    }

    // Back handling: pop to dashboard if on sub-screens
    BackHandler(enabled = currentTab != AuraTab.DASHBOARD) {
        viewModel.selectTab(AuraTab.DASHBOARD)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = AuraBackgroundDark,
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            NavigationBar(
                containerColor = AuraSurfaceDark,
                contentColor = AuraTextPrimary,
                tonalElevation = 8.dp,
                windowInsets = WindowInsets.navigationBars,
                modifier = Modifier.testTag("aura_bottom_navigation")
            ) {
                val tabs = listOf(
                    Triple(AuraTab.DASHBOARD, Icons.Default.Dashboard, "Core"),
                    Triple(AuraTab.INBOX, Icons.Default.Email, "Inbox"),
                    Triple(AuraTab.AUTOMATION, Icons.Default.PhoneAndroid, "Control"),
                    Triple(AuraTab.INTEL, Icons.Default.Public, "Intel"),
                    Triple(AuraTab.STUDY, Icons.Default.School, "Study"),
                    Triple(AuraTab.MAKER, Icons.Default.Code, "Maker"),
                    Triple(AuraTab.SETTINGS, Icons.Default.Person, "Profile")
                )

                tabs.forEach { (tab, icon, label) ->
                    NavigationBarItem(
                        selected = currentTab == tab,
                        onClick = { viewModel.selectTab(tab) },
                        icon = {
                            Icon(
                                imageVector = icon,
                                contentDescription = label,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        label = {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AuraBackgroundDark,
                            selectedTextColor = AuraCyanBright,
                            indicatorColor = AuraCyanBright,
                            unselectedIconColor = AuraTextMuted,
                            unselectedTextColor = AuraTextMuted
                        ),
                        modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { launchVoiceRecognition() },
                containerColor = AuraCyanBright,
                contentColor = AuraBackgroundDark,
                shape = CircleShape,
                modifier = Modifier.testTag("floating_voice_fab")
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Voice Directive",
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(AuraBackgroundDark)
        ) {
            Crossfade(targetState = currentTab, label = "tab_crossfade") { tab ->
                when (tab) {
                    AuraTab.DASHBOARD -> DashboardScreen(
                        viewModel = viewModel,
                        onOpenVoiceDialog = { launchVoiceRecognition() }
                    )
                    AuraTab.INBOX -> CommsInboxScreen(viewModel = viewModel)
                    AuraTab.AUTOMATION -> MobileControlScreen(viewModel = viewModel)
                    AuraTab.INTEL -> GlobalIntelScreen(viewModel = viewModel)
                    AuraTab.STUDY -> StudyVaultScreen(viewModel = viewModel)
                    AuraTab.MAKER -> MakerStudioScreen(viewModel = viewModel)
                    AuraTab.SETTINGS -> SettingsScreen(viewModel = viewModel)
                }
            }

            // Voice Command Dialog Overlay
            if (showVoiceDialog) {
                VoiceCommandDialog(
                    voiceState = voiceState,
                    isSpeaking = isSpeaking,
                    onDismiss = {
                        showVoiceDialog = false
                        viewModel.dismissVoiceDialog()
                    },
                    onSubmitCommand = { cmd ->
                        viewModel.processVoiceCommand(cmd)
                    },
                    onStopSpeaking = { viewModel.stopSpeaking() }
                )
            }
        }
    }
}
