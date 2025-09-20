package br.dev.singular.overview.presentation.ui.utils

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import br.dev.singular.overview.presentation.R

@Composable
fun Modifier.border(
    isVisible: Boolean = true,
    color: Color = DarkGray,
    width: Dp = dimensionResource(R.dimen.border_width)
): Modifier = composed {
    if (isVisible) {
        border(width, color, RoundedCornerShape(dimensionResource(R.dimen.corner_width)))
    } else {
        this
    }
}