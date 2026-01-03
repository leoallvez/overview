package br.dev.singular.overview.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.dev.singular.overview.presentation.ui.components.text.UiText
import br.dev.singular.overview.presentation.ui.components.text.UiTitle
import br.dev.singular.overview.presentation.ui.theme.Background
import br.dev.singular.overview.presentation.ui.theme.HighlightColor

/**
 * A composable that fills the maximum size available and centers its children both vertically and
 * horizontally.
 *
 * This composable is useful for displaying loading indicators, error messages, or any other content
 * that should be centered on the screen.
 *
 * @param modifier The modifier to be applied to the [Column].
 * @param content The content to be displayed inside the [Column].
 */
@Composable
fun UiCenteredColumn(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = modifier
            .background(Background)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        content()
    }
}

@Preview
@Composable
internal fun UiCenteredColumnPreview() {
    UiCenteredColumn {
        UiTitle(text = "Wanning!", color = HighlightColor)
        UiText("This is a centralized content.")
    }
}
