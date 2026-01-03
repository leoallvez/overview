package br.dev.singular.overview.presentation.ui.components.icon.style

import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.theme.DefaultTextColor

/**
 * Represents the style of an icon to be applied to a composable.
 *
 * @property source The source of the icon, which can be a vector or a painter resource.
 * @property modifier The modifier to be applied to the icon.
 * @property color The tint color of the icon.
 * @property sizeRes The size of the icon as a dimension resource.
 * @property descriptionRes The content description of the icon as a string resource.
 */
data class UiIconStyle(
    val source: UiIconSource,
    val modifier: Modifier = Modifier,
    val color: Color = DefaultTextColor,
    @param:DimenRes val sizeRes: Int = R.dimen.spacing_8x,
    @param:StringRes val descriptionRes: Int? = null,
)
