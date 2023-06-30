package br.com.deepbyte.overview.util

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.deepbyte.overview.R
import br.com.deepbyte.overview.ui.theme.Gray
import br.com.deepbyte.overview.ui.theme.PrimaryBackground

fun Modifier.defaultBorder(color: Color = Gray) = composed {
    border(
        color = color,
        width = dimensionResource(R.dimen.border_width),
        shape = RoundedCornerShape(dimensionResource(R.dimen.corner))
    )
}

fun Modifier.defaultBackground() = background(PrimaryBackground)

fun Modifier.defaultPadding(
    start: Dp = 10.dp,
    top: Dp = 5.dp,
    end: Dp = 10.dp,
    bottom: Dp = 5.dp
) = padding(start, top, end, bottom)
