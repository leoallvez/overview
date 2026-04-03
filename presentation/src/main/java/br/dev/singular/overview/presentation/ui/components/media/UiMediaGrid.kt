package br.dev.singular.overview.presentation.ui.components.media

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Devices.PIXEL_3A_XL
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.compose.LazyPagingItems
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.ui.components.shimmer.UiShimmerProvider
import br.dev.singular.overview.presentation.ui.utils.fakeMedias
import kotlinx.collections.immutable.ImmutableList

/**
 * A composable that displays a grid of media items with pagination support.
 *
 * @param items The [LazyPagingItems] containing the media items to display.
 * @param modifier The modifier to be applied to this grid.
 * @param onClick The callback to be executed when a media item is clicked.
 */
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
            key = { index -> items.peek(index)?.uiId ?: index }
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

/**
 * A composable that displays a grid of media items.
 *
 * @param items The list of [MediaUiModel] to display.
 * @param modifier The modifier to be applied to this grid.
 * @param onClick The callback to be executed when a media item is clicked.
 */
@Composable
fun UiMediaGrid(
    items: ImmutableList<MediaUiModel>,
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

/**
 * A skeleton placeholder for [UiMediaGrid] to be used during loading states.
 *
 * @param modifier The modifier to be applied to this grid.
 * @param itemCount The number of skeleton items to display.
 */
@Composable
fun UiMediaGridSkeleton(
    modifier: Modifier = Modifier,
    itemCount: Int = 100
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(
            minSize = dimensionResource(R.dimen.poster_width)
        ),
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement
            .spacedBy(dimensionResource(R.dimen.spacing_1x)),
        verticalArrangement = Arrangement
            .spacedBy(dimensionResource(R.dimen.spacing_1x))
    ) {
        items(itemCount) {
            UiMediaItemSkeleton()
        }
    }
}

@Preview(
    name = "Vertical",
    device = PIXEL_3A_XL
)
@Composable
internal fun UiMediaGridVerticalPreview() {
    UiMediaGrid(
        items = fakeMedias(90),
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_1x))
    )
}

@Preview(
    name = "Horizontal",
    device = "spec:parent=pixel_3a_xl,orientation=landscape",
)
@Composable
internal fun UiMediaGridHorizontalPreview() {
    UiMediaGrid(
        items = fakeMedias(90),
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_1x))
    )
}

@Preview(
    name = "Skeleton",
    device = PIXEL_3A_XL
)
@Composable
internal fun UiMediaGridSkeletonPreview() {
    UiShimmerProvider {
        UiMediaGridSkeleton()
    }
}

@Preview(
    name = "Horizontal Skeleton",
    device = "spec:parent=pixel_3a_xl,orientation=landscape",
)
@Composable
internal fun UiMediaGridSkeletonHorizontalPreview() {
    UiShimmerProvider {
        UiMediaGridSkeleton()
    }
}
