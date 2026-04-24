package br.dev.singular.overview.presentation.ui.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.icon.UiIconButton
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconStyle
import br.dev.singular.overview.presentation.ui.components.style.UiBorderStyle
import br.dev.singular.overview.presentation.ui.theme.LowlightColor
import br.dev.singular.overview.presentation.ui.theme.Surface
import br.dev.singular.overview.presentation.ui.theme.WarningColor
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview

private const val ANIMATION_DURATION_MILLIS = 200
private const val LIKED_ICON_SCALE = 1f
private const val UNLIKED_ICON_SCALE = 0.9f
private const val BUTTON_BACKGROUND_ALPHA = 0.9f

/**
 * A button that allows the user to "like" or "unlike" something.
 *
 * @param modifier The modifier to be applied to this component.
 * @param isLiked Whether the button is currently in the "liked" state.
 * @param onClick The callback to be executed when the button is clicked.
 */
@Composable
fun UiLikeButton(
    modifier: Modifier = Modifier,
    isLiked: Boolean,
    onClick: () -> Unit
) {
    val (iconStyle, borderStyle) = getLikeButtonStyles(isLiked = isLiked)
    UiIconButton(
        modifier = modifier,
        background = Surface.copy(alpha = BUTTON_BACKGROUND_ALPHA),
        iconStyle = iconStyle,
        borderStyle = borderStyle,
        onClick = onClick
    )
}

@Composable
private fun getLikeButtonStyles(isLiked: Boolean): Pair<UiIconStyle, UiBorderStyle> {
    val transition = updateTransition(targetState = isLiked, label = "LikeTransition")

    val color by transition.animateColor(
        transitionSpec = { tween(durationMillis = ANIMATION_DURATION_MILLIS) },
        label = "LikeColor"
    ) { state ->
        if (state) WarningColor else LowlightColor
    }

    val scale by transition.animateFloat(
        transitionSpec = {
            tween(
                durationMillis = ANIMATION_DURATION_MILLIS,
                easing = LinearEasing
            )
        },
        label = "LikeScale"
    ) { state ->
        if (state) LIKED_ICON_SCALE else UNLIKED_ICON_SCALE
    }

    val iconSizeRes = if (isLiked) {
        R.dimen.spacing_6x
    } else {
        R.dimen.spacing_5x
    }

    val iconStyle = UiIconStyle(
        sizeRes = iconSizeRes,
        color = color,
        modifier = Modifier.scale(scale),
        descriptionRes = R.string.like_button,
        source = UiIconSource.vector(icon = Icons.Default.Favorite),
    )

    val borderStyle = UiBorderStyle(color = color, visible = true)

    return iconStyle to borderStyle
}

@UiComponentPreview
@Composable
private fun UiLikeButtonPreview() {
    var isLiked by remember { mutableStateOf(false) }
    UiLikeButton(isLiked = isLiked) {
        isLiked = !isLiked
    }
}
