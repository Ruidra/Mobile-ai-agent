package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

@Composable
fun VoiceOrb(
    isListening: Boolean,
    isSpeaking: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "orb_pulse")
    
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val activeColor = when {
        isSpeaking -> AuraIndigo
        isListening -> AuraCyanBright
        else -> AuraCyan
    }

    Box(
        modifier = modifier
            .size(160.dp)
            .testTag("voice_orb_container")
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        // Glowing orbital rings
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .scale(if (isListening || isSpeaking) pulseScale else 1f)
        ) {
            val center = Offset(size.width / 2, size.height / 2)
            val radius = size.minDimension / 2

            // Outer glow ring
            drawCircle(
                color = activeColor.copy(alpha = 0.15f),
                radius = radius * 0.96f,
                center = center,
                style = Stroke(width = 3.dp.toPx())
            )

            // Mid segmented orbit
            drawCircle(
                color = AuraIndigo.copy(alpha = 0.35f),
                radius = radius * 0.78f,
                center = center,
                style = Stroke(width = 2.dp.toPx())
            )

            // Inner core ring
            drawCircle(
                color = activeColor.copy(alpha = 0.6f),
                radius = radius * 0.62f,
                center = center,
                style = Stroke(width = 1.5.dp.toPx())
            )
        }

        // Inner glowing core
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            activeColor.copy(alpha = 0.9f),
                            AuraIndigo.copy(alpha = 0.7f),
                            AuraBackgroundDark.copy(alpha = 0.95f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isSpeaking) Icons.Default.GraphicEq else Icons.Default.Mic,
                contentDescription = "Aura Voice Trigger",
                tint = AuraTextPrimary,
                modifier = Modifier
                    .size(36.dp)
                    .testTag("voice_orb_icon")
            )
        }
    }
}
