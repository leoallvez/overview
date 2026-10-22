package br.dev.singular.overview.presentation.ui.screens.media.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.theme.DefaultTextColor
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview

@Composable
fun UiMediaInfoItem(
    modifier: Modifier = Modifier,
    label: String = "",
    value: String,
    color: Color = DefaultTextColor,
) {
    if (value.isBlank()) return

    val text = buildAnnotatedString {
        if (label.isNotBlank()) {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("$label: ")
            }
        }
        append(value)
    }

    Text(
        text = text,
        color = color,
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier
    )
}

@UiComponentPreview
@Composable
internal fun UiMediaInfoItemPreview() {
    Column(
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_4x))
    ) {
        UiMediaInfoItem(
            label = "Released",
            value = "12/12/2024"
        )
        UiMediaInfoItem(
            label = "Genres",
            value = "Action, Adventure, Science Fiction",
            color = HighlightColor
        )
        UiMediaInfoItem(
            value = "Item without label",
            color = Color.Gray
        )
    }
}
