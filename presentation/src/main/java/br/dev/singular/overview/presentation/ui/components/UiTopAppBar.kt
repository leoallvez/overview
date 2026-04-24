package br.dev.singular.overview.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.icon.UiIconButton
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconStyle
import br.dev.singular.overview.presentation.ui.components.text.UiTitle
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview

/**
 * A custom toolbar composable that displays a title.
 *
 * @param title The title to be displayed in the center of the toolbar.
 * @param modifier The [Modifier] to be applied to the toolbar.
 */
@Composable
fun UiTopAppBar(
    title: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.spacing_14x))
    ) {
        UiTitle(
            text = title,
            modifier = Modifier.align(Alignment.CenterStart),
            color = HighlightColor
        )
    }
}

@Composable
fun UiTopAppBar(title: String, onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.spacing_14x)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        UiTitle(
            text = title,
            modifier = Modifier.weight(1f),
            color = HighlightColor
        )
        UiIconButton(
            iconStyle = UiIconStyle(
                source = UiIconSource.painter(R.drawable.ic_arrow_down),
                sizeRes = R.dimen.spacing_8x,
            ),
            onClick = onBack
        )
    }
}

@UiComponentPreview
@Composable
private fun UiToolbarDefaultPreview() {
    UiTopAppBar(title = "Title")
}

@UiComponentPreview
@Composable
private fun UiToolbarWithCloseButtonPreview() {
    UiTopAppBar(title = "Title") { }
}
