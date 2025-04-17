package br.dev.singular.overview.presentation.ui.utils

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.theme.Gray

@Composable
fun Modifier.border(
    withBorder: Boolean,
    color: Color = Gray,
    width: Dp = dimensionResource(R.dimen.border)
): Modifier = composed {
    if (withBorder) {
        border(width, color, RoundedCornerShape(dimensionResource(R.dimen.corner)))
    } else {
        this
    }
}