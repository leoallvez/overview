package br.dev.singular.overview.presentation.ui.components.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.theme.DefaultTextColor

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

@Preview
@Composable
internal fun UiTextPreview() {
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