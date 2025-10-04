package br.dev.singular.overview.presentation.ui.components.media

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onClick: (MediaUiModel) -> Unit = {}
) {
    if (items.isNotEmpty()) {
        Column(modifier = modifier) {
            UiTitle(title, modifier = Modifier.padding(contentPadding))
            LazyRow(
                horizontalArrangement = Arrangement
                    .spacedBy(dimensionResource(R.dimen.spacing_1x)),
                contentPadding = contentPadding
            ) {
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

@Preview(name = "With Content Padding")
@Composable
internal fun UiMediaListWitchContentPaddingPreview() {
    UiMediaList(
        title = "Imagined Movies",
        contentPadding = PaddingValues(start = dimensionResource(R.dimen.spacing_4x)),
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
