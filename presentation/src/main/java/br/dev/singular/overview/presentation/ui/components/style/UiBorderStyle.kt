package br.dev.singular.overview.presentation.ui.components.style

import androidx.annotation.DimenRes
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.theme.BorderColor

/**
 * Represents the style of a border to be applied to a composable.
 *
 * @property visible Whether the border is visible.
 * @property color The color of the border.
 * @property width The width of the border as a dimension resource.
 * @property shape The shape of the border.
 */
@Immutable
data class UiBorderStyle(
    val visible: Boolean = true,
    val color: Color = BorderColor,
    @param:DimenRes val width: Int = R.dimen.border_width,
    val shape: Shape = RoundedCornerShape(5.dp)
)
