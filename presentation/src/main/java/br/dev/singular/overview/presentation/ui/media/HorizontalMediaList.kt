package br.dev.singular.overview.presentation.ui.media

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
import br.dev.singular.overview.presentation.model.MediaUIModel
import br.dev.singular.overview.presentation.ui.text.SectionTitle
import br.dev.singular.overview.presentation.ui.theme.PrimaryBackground
import br.dev.singular.overview.presentation.ui.utils.getMediaMocks

@Composable
fun HorizontalMediaList(
    title: String,
    modifier: Modifier = Modifier,
    items: List<MediaUIModel>,
    onClick: (MediaUIModel) -> Unit = {}
) {
    if (items.isNotEmpty()) {
        val spacingXS = dimensionResource(R.dimen.spacing_xs)
        Column(
            modifier = modifier
                .background(PrimaryBackground)
                .padding(vertical = spacingXS)
        ) {
            SectionTitle(title)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(spacingXS)) {
                items(items) { item ->
                    MediaItem(item, onClick)
                }
            }
        }
    }
}

@Preview
@Composable
fun FilledHorizontalMediaListPreview() {
    HorizontalMediaList(
        title = "My Favorites Movies",
        items = getMediaMocks()
    )
}

@Preview
@Composable
fun EmptyHorizontalMediaListPreview() {
    HorizontalMediaList(
        title = "Empty List",
        items = listOf()
    )
}

