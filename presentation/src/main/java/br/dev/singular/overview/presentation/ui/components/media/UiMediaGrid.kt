package br.dev.singular.overview.presentation.ui.components.media

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.compose.LazyPagingItems
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
            .spacedBy(dimensionResource(R.dimen.spacing_1x))
    ) {
        items(
            items.itemCount,
            key = { index -> items.peek(index)?.id ?: index }
        ) { index ->
            items[index]?.let {
                UiMediaItem(it,
                    onClick = onClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(2f / 3f)
                )
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
            .spacedBy(dimensionResource(R.dimen.spacing_1x))
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
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_1x))
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
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_1x))
    )
}