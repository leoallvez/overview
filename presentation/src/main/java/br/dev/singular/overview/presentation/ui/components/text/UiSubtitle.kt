package br.dev.singular.overview.presentation.ui.components.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.theme.DefaultTextColor

/**
 * A composable that displays a subtitle text.
 *
 * @param text The text to be displayed.
 * @param modifier The modifier to be applied to this component.
 * @param display Whether to display the subtitle.
 * @param isBold Whether the text should be bold.
 * @param color The color of the text.
 */
@Composable
fun UiSubtitle(
    text: String,
    modifier: Modifier = Modifier,
    display: Boolean = true,
    isBold: Boolean = true,
    color: Color = DefaultTextColor
) {
    if (display && text.isNotEmpty()) {
        Text(
            text = text,
            modifier = modifier,
            color = color,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Preview
@Composable
internal fun UiSubtitlePreview() {
    Column {
        UiSubtitle(text = "Text One")
        UiSubtitle(text = "Text Two", isBold = true)
        UiSubtitle(text = "Text Three", color = Color.Gray)
        UiSubtitle(
            text = "Hey! You should not see this text!",
            color = Color.Red,
            display = false
        )
        UiSubtitle(
            text = "Text Four",
            modifier = Modifier
                .background(Color.White)
                .padding(dimensionResource(R.dimen.spacing_1x)),
            color = Color.Black
        )
    }
}
