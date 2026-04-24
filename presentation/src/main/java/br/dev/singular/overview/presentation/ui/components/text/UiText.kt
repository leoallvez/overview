package br.dev.singular.overview.presentation.ui.components.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.shimmer.UiShimmerBox
import br.dev.singular.overview.presentation.ui.components.shimmer.UiShimmerProvider
import br.dev.singular.overview.presentation.ui.theme.DefaultTextColor
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview

/**
 * A composable that displays a text with default styling, including ellipsis for overflow.
 *
 * @param text The text to be displayed.
 * @param modifier The modifier to be applied to this component.
 * @param style The style of the text.
 * @param color The color of the text.
 * @param textAlign The alignment of the text.
 * @param isBold Whether the text should be bold.
 */
@Composable
fun UiText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = DefaultTextColor,
    textAlign: TextAlign = TextAlign.Center,
    isBold: Boolean = false
) {
    Text(
        color = color,
        text = text,
        modifier = modifier,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
        textAlign = textAlign,
        fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
        style = style
    )
}

/**
 * A skeleton placeholder for [UiText].
 *
 * @param modifier The modifier to be applied to this component.
 */
@Composable
fun UiTextSkeleton(
    modifier: Modifier = Modifier
) {
    UiShimmerBox(
        modifier = modifier
            .fillMaxWidth()
            .height(16.dp)
    )
}

@UiComponentPreview
@Composable
private fun UiTextPreview() {
    Column {
        UiText(text = "Text One")
        UiText(text = "Text Two", isBold = true)
        UiText(text = "Text Three", color = Color.Gray)
        UiText(
            text = "Text Four",
            modifier = Modifier
                .background(Color.White)
                .padding(dimensionResource(R.dimen.spacing_1x)),
            color = Color.Black
        )
    }
}

@UiComponentPreview
@Composable
private fun UiTextSkeletonPreview() {
    UiShimmerProvider {
        Column(
            modifier = Modifier
                .width(100.dp)
                .background(Color.Black)
        ) {
            UiTextSkeleton(modifier = Modifier.padding(bottom = 8.dp))
            UiTextSkeleton(modifier = Modifier.fillMaxWidth(0.6f))
        }
    }
}
