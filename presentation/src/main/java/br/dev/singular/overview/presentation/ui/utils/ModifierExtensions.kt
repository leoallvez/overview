package br.dev.singular.overview.presentation.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.theme.Background
import br.dev.singular.overview.presentation.ui.theme.Surface

@Composable
fun Modifier.border(
    isVisible: Boolean = true,
    color: Color = DarkGray,
    width: Dp = dimensionResource(R.dimen.border_width),
    shape: RoundedCornerShape = RoundedCornerShape(dimensionResource(R.dimen.corner_width))
): Modifier {
    return if (isVisible) {
        border(width, color, shape)
    } else {
        this
    }
}

@Composable
fun Modifier.defaultBackground() = background(Background)

@Composable
fun Modifier.surfaceBackground() = background(Surface)
