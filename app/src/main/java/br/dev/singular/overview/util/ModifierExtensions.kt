package br.dev.singular.overview.util

import androidx.annotation.DimenRes
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.dev.singular.overview.R
import br.dev.singular.overview.ui.theme.Gray

fun Modifier.defaultBorder(color: Color = Gray, @DimenRes corner: Int = R.dimen.corner) = composed {
    val width = dimensionResource(R.dimen.border_width)
    val shape = RoundedCornerShape(dimensionResource(corner))
    then(border(color = color, width = width, shape = shape))
}

fun Modifier.defaultPadding(
    start: Dp = 10.dp,
    top: Dp = 5.dp,
    end: Dp = 10.dp,
    bottom: Dp = 5.dp
) = then(padding(start, top, end, bottom))

fun Modifier.onClick(active: Boolean, onClick: () -> Unit) = if (active) {
    clickable { onClick.invoke() }
} else {
    this
}
