package br.dev.singular.overview.presentation.ui.components.media

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.ui.utils.getMediaMocks

@Composable
fun UiMediaGrid(
    items: LazyPagingItems<MediaUiModel>,
    modifier: Modifier = Modifier,
    onClick: (MediaUiModel) -> Unit = {}
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = dimensionResource(R.dimen.poster_width)),
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement
            .spacedBy(dimensionResource(R.dimen.spacing_extra_small))
    ) {
        items(
            items.itemCount,
            key = items.itemKey { it.id }
        ) { index ->
            items[index]?.let {
                UiMediaItem(it, onClick = onClick)
            }
        }
    }
}

@Composable
fun UiMediaGrid(
    items: List<MediaUiModel>,
    modifier: Modifier = Modifier,
    onClick: (MediaUiModel) -> Unit = {}
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = dimensionResource(R.dimen.poster_width)),
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement
            .spacedBy(dimensionResource(R.dimen.spacing_extra_small))
    ) {
        items(items.size) { index ->
            UiMediaItem(items[index], onClick = onClick)
        }
    }
}

@Preview(
    name = "Vertical",
    widthDp = 411,
    heightDp = 960
)
@Composable
internal fun UiMediaGridVerticalPreview() {
    UiMediaGrid(
        items = getMediaMocks(90),
        modifier = Modifier.padding(5.dp)
    )
}

@Preview(
    name = "Horizontal",
    widthDp = 960,
    heightDp = 411
)
@Composable
internal fun UiMediaGridHorizontalPreview() {
    UiMediaGrid(
        items = getMediaMocks(90),
        modifier = Modifier.padding(5.dp)
    )
}