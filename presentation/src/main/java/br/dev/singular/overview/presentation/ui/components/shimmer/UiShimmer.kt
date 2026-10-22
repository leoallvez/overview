package br.dev.singular.overview.presentation.ui.components.shimmer

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.StartOffsetType
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode

private val LocalShimmerOffset = compositionLocalOf { 0f }

@Composable
fun UiShimmerProvider(
    key: String? = null,
    content: @Composable () -> Unit
) {
    key(key) {
        val transition = rememberInfiniteTransition(label = "global_shimmer")
        val offset by transition.animateFloat(
            initialValue = -1000f,
            targetValue = 2000f,
            animationSpec = infiniteRepeatable(
                animation = tween(1500, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
                initialStartOffset = StartOffset(500, StartOffsetType.Delay)
            ),
            label = "offset"
        )

        CompositionLocalProvider(LocalShimmerOffset provides offset) {
            content()
        }
    }
}

@Composable
internal fun Modifier.shimmer(): Modifier {
    val globalOffset = LocalShimmerOffset.current

    return background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFF121212),
                Color(0xFF1A1A1A),
                Color(0xFF242424),
                Color(0xFF1A1A1A),
                Color(0xFF121212)
            ),
            start = Offset(globalOffset, 0f),
            end = Offset(globalOffset + 1000f, 0f),
            tileMode = TileMode.Clamp
        )
    )
}
