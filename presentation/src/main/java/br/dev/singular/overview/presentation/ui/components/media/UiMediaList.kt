package br.dev.singular.overview.presentation.ui.components.media

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.ui.components.text.UiTitle
import br.dev.singular.overview.presentation.ui.utils.defaultBackground
import br.dev.singular.overview.presentation.ui.utils.getMediaMocks

@Composable
fun UiMediaList(
    title: String,
    modifier: Modifier = Modifier,
    items: List<MediaUiModel>,
    onClick: (MediaUiModel) -> Unit = {}
) {
    if (items.isNotEmpty()) {
        val spacingXS = dimensionResource(R.dimen.spacing_xs)
        Column(modifier = modifier.padding(vertical = spacingXS)) {
            UiTitle(title)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(spacingXS)) {
                items(items) { item ->
                    UiMediaItem(item, onClick = onClick)
                }
            }
        }
    }
}

@Preview(name = "Filled")
@Composable()
internal fun UiMediaPreview() {
    UiMediaList(
        title = "Imagined Movies",
        items = getMediaMocks()
    )
}

@Preview(name = "Filled with background")
@Composable
internal fun UiMediaListWithBackgroundPreview() {
    UiMediaList(
        title = "Imagined Movies",
        modifier = Modifier.defaultBackground(),
        items = getMediaMocks()
    )
}

@Preview("Empty List (can't show anything)")
@Composable
internal fun UiMediaListEmptyPreview() {
    UiMediaList(
        title = "Empty List",
        modifier = Modifier.defaultBackground(),
        items = listOf()
    )
}
