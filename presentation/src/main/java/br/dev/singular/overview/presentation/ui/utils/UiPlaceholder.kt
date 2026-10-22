package br.dev.singular.overview.presentation.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.text.UiText

/**
 * A helper component to visualize slots or missing content in previews and tests.
 * It displays a dashed border, a diagonal hatch pattern, and a text in the center.
 */
@Composable
internal fun UiPlaceholder(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF9D00FF),
) {
    val backgroundColor = color.copy(alpha = 0.15f)
    val strokeColor = color.copy(alpha = 0.5f)
    val textColor = Color.White.copy(alpha = 0.9f)
    val cornerRadius = dimensionResource(R.dimen.spacing_1x)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = dimensionResource(R.dimen.spacing_10x))
            .background(backgroundColor, RoundedCornerShape(cornerRadius))
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                val dash = 10f

                // 1. Dashed border
                drawRoundRect(
                    color = strokeColor,
                    style = Stroke(
                        width = strokeWidth,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(dash, dash), 0f)
                    ),
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )

                // 2. Hatch pattern (Diagonal lines)
                val step = 20.dp.toPx()
                clipRect {
                    var x = -size.height
                    while (x < size.width) {
                        drawLine(
                            color = strokeColor.copy(alpha = 0.2f),
                            start = Offset(x, 0f),
                            end = Offset(x + size.height, size.height),
                            strokeWidth = strokeWidth
                        )
                        x += step
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        UiText(
            text = text,
            color = textColor,
            isBold = true,
            style = TextStyle(
                fontFamily = FontFamily.Monospace,
                letterSpacing = 2.sp,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(dimensionResource(R.dimen.spacing_2x))
        )
    }
}

@UiComponentPreview
@Composable
private fun UiPlaceholderPreview() {
    UiPlaceholder(text = "Placeholder Preview")
}
