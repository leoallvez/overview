package br.dev.singular.overview.presentation.ui.components.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.theme.DefaultTextColor

@Composable
fun UiParagraph(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier,
        color = DefaultTextColor,
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Justify
    )
}

@Preview
@Composable
internal fun UiParagraphPreview() {
    UiParagraph(text = stringResource(R.string.lorem_ipsum))
}
