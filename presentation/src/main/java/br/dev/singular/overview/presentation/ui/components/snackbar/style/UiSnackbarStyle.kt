package br.dev.singular.overview.presentation.ui.components.snackbar.style

import androidx.compose.ui.graphics.Color
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.snackbar.UiSnackbar
import br.dev.singular.overview.presentation.ui.theme.DefaultTextColor
import com.composables.icons.lucide.CircleCheck
import com.composables.icons.lucide.CircleX
import com.composables.icons.lucide.Lucide

/**
 * Visual style variants for [UiSnackbar] matching dark surface cards with accent borders and gradients.
 */
enum class UiSnackbarStyle(
    val accentColor: Color,
    val borderColor: Color,
    val containerStartColor: Color,
    val containerEndColor: Color,
    val closeBackgroundColor: Color,
    val closeIconColor: Color,
    val actionTextColor: Color,
    val messageTextColor: Color,
    val titleTextColor: Color = DefaultTextColor,
    val defaultIcon: UiIconSource,
) {
    Success(
        accentColor = Color(0xFF1ED760),
        borderColor = Color(0xFF1ED760),
        containerStartColor = Color(0xFF123524),
        containerEndColor = Color(0xFF0F2E1E),
        closeBackgroundColor = Color(0xFF1F3D2B),
        closeIconColor = Color(0xFF9FD7B5),
        actionTextColor = Color(0xFF1ED760),
        messageTextColor = Color(0xFFC8E6D3),
        titleTextColor = DefaultTextColor,
        defaultIcon = UiIconSource.vector(Lucide.CircleCheck)
    ),
    Error(
        accentColor = Color(0xFFFF4D57),
        borderColor = Color(0xFFB91C1C),
        containerStartColor = Color(0xFF3A0408),
        containerEndColor = Color(0xFF280205),
        closeBackgroundColor = Color(0xFF5A1015),
        closeIconColor = Color(0xFFFFB4B4),
        actionTextColor = Color(0xFFFF6B6B),
        messageTextColor = Color(0xFFF3EAEA),
        titleTextColor = DefaultTextColor,
        defaultIcon = UiIconSource.vector(Lucide.CircleX)
    );
}
