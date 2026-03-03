package br.dev.singular.overview.presentation.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode

private val LocalShimmerOffset = staticCompositionLocalOf { 0f }

@Composable
fun UiShimmerProvider(content: @Composable () -> Unit) {
    val transition = rememberInfiniteTransition(label = "global_shimmer")
    val offset by transition.animateFloat(
        initialValue = -1000f,
        targetValue = 2000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset"
    )

    CompositionLocalProvider(LocalShimmerOffset provides offset) {
        content()
    }
}

@Composable
fun Modifier.syncedShimmer(): Modifier {
    val globalOffset = LocalShimmerOffset.current

    return background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color.Black,
                Color(0xFF2C2C2C),
                Color(0xFF424242),
                Color(0xFF2C2C2C),
                Color.Black
            ),
            start = Offset(globalOffset, globalOffset),
            end = Offset(globalOffset + 500f, globalOffset + 500f),
            tileMode = TileMode.Clamp
        )
    )
}
