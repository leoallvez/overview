package br.dev.singular.overview.presentation.ui.utils

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import br.dev.singular.overview.presentation.ui.theme.LowlightColor

/**
 * Animates between an active and inactive color based on the [isActive] state.
 *
 * @param isActive The state that determines which color to animate to.
 * @param activeColor The color to use when [isActive] is true. Defaults to [HighlightColor].
 * @param inactiveColor The color to use when [isActive] is false. Defaults to [LowlightColor].
 * @return The animated [Color] state.
 */
@Composable
internal fun animateActionColor(
    isActive: Boolean,
    activeColor: Color = HighlightColor,
    inactiveColor: Color = LowlightColor
): Color {
    val color by animateColorAsState(
        targetValue = if (isActive) activeColor else inactiveColor,
        animationSpec = tween(durationMillis = 200),
        label = "ActionColorAnimation"
    )
    return color
}
