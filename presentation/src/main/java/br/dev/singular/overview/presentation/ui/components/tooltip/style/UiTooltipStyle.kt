package br.dev.singular.overview.presentation.ui.components.tooltip.style

import androidx.compose.ui.graphics.Color
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import com.composables.icons.lucide.CircleAlert
import com.composables.icons.lucide.Lucide


data class UiTooltipStyle(
    val accentColor: Color = HighlightColor,
    val borderColor: Color = Color(0xFF005F73),
    val containerStartColor: Color = Color(0xFF0C2D37),
    val containerEndColor: Color = Color(0xFF081C24),
    val closeBackgroundColor: Color = Color(0xFF163C4A),
    val closeIconColor: Color = HighlightColor,
    val messageTextColor: Color = Color(0xFFC0E0EC),
    val defaultIcon: UiIconSource = UiIconSource.vector(Lucide.CircleAlert)
)
