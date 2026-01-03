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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.theme.DefaultTextColor

/**
 * A composable that displays a title text with bold styling and ellipsis for overflow.
 *
 * @param text The text to be displayed.
 * @param modifier The modifier to be applied to this component.
 * @param textAlign The alignment of the text.
 * @param color The color of the text.
 */
@Composable
fun UiTitle(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
    color: Color = DefaultTextColor
) {
    Text(
        text = text,
        color = color,
        textAlign = textAlign,
        modifier = modifier.padding(vertical = dimensionResource(R.dimen.spacing_1x)),
        style = MaterialTheme.typography.titleLarge,
        overflow = TextOverflow.Ellipsis,
        maxLines = 2,
        fontWeight = FontWeight.Bold
    )
}

@Preview
@Composable
internal fun UiTitlePreview() {
    Column {
        UiTitle(text = "Title One")
        UiTitle(text = "Title Two", color = Color.Gray)
        UiTitle(
            text = "Title Three",
            modifier = Modifier
                .background(Color.White)
                .padding(vertical = dimensionResource(R.dimen.spacing_2x)),
            color = Color.Black
        )
    }
}
