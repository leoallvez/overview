package br.dev.singular.overview.presentation.ui.components.button.style

import androidx.annotation.DimenRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.style.UiBorderStyle
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import br.dev.singular.overview.presentation.ui.theme.LowlightColor
import br.dev.singular.overview.presentation.ui.utils.animateActionColor

@Stable
internal interface UiButtonColors {
    @Composable
    fun backgroundColor(enabled: Boolean): Color
    @Composable
    fun borderColor(enabled: Boolean): Color
    @Composable
    fun textColor(enabled: Boolean): Color
}

@Stable
internal interface UiButtonStyle {
    val border: UiBorderStyle
    val height: Int
    val horizontalPadding : Int
    val cornerRadius: Int
    val colors: UiButtonColors
}

@Immutable
internal data class UiActionButtonColors(
    val activeBackgroundColor: Color = Color.Transparent,
    val inactiveBackgroundColor: Color = LowlightColor.copy(alpha = 0.12f),
    val activeBorderColor: Color = HighlightColor,
    val inactiveBorderColor: Color = LowlightColor.copy(alpha = 0.3f),
    val activeTextColor: Color = HighlightColor,
    val inactiveTextColor: Color = LowlightColor.copy(alpha = 0.3f),
) : UiButtonColors {

    @Composable
    override fun backgroundColor(enabled: Boolean): Color {
        return animateActionColor(
            isActive = enabled,
            activeColor = activeBackgroundColor,
            inactiveColor = inactiveBackgroundColor
        )
    }

    @Composable
    override fun borderColor(enabled: Boolean): Color {
        return animateActionColor(
            isActive = enabled,
            activeColor = activeBorderColor,
            inactiveColor = inactiveBorderColor
        )
    }

    @Composable
    override fun textColor(enabled: Boolean): Color {
        return animateActionColor(
            isActive = enabled,
            activeColor = activeTextColor,
            inactiveColor = inactiveTextColor
        )
    }
}

@Immutable
internal data class UiActionButtonStyle(
    override val border: UiBorderStyle = UiBorderStyle(),
    @param:DimenRes override val height: Int = R.dimen.button_height,
    @param:DimenRes override val cornerRadius: Int = R.dimen.button_corner_radius,
    override val colors: UiButtonColors = UiActionButtonColors(),
    override val horizontalPadding: Int = R.dimen.spacing_1x
) : UiButtonStyle

@Immutable
internal class UiGoogleButtonStyle : UiButtonStyle by UiActionButtonStyle() {
    override val horizontalPadding = R.dimen.spacing_6x
    override val colors = UiActionButtonColors(
        activeBackgroundColor = Color.White,
        activeBorderColor = Color(0xFF747775),
        inactiveBorderColor = Color(0xFF1F1F1F),
        activeTextColor = Color(0xFF1F1F1F),
        inactiveTextColor = Color(0xFF1F1F1F)
    )
}
