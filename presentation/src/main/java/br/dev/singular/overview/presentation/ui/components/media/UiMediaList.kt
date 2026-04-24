package br.dev.singular.overview.presentation.ui.components.media

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.ui.components.shimmer.UiShimmerBox
import br.dev.singular.overview.presentation.ui.components.shimmer.UiShimmerProvider
import br.dev.singular.overview.presentation.ui.components.text.UiTitle
import br.dev.singular.overview.presentation.ui.utils.UiScreenPreview
import br.dev.singular.overview.presentation.ui.utils.defaultBackground
import br.dev.singular.overview.presentation.ui.utils.fakeMedias
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

/**
 * A composable that displays a horizontal list of media items with a title.
 *
 * @param title The title to be displayed above the list.
 * @param modifier The modifier to be applied to this component.
 * @param items The immutable list of [MediaUiModel] to be displayed.
 * @param contentPadding The padding to be applied to the content.
 * @param onClick The callback to be executed when a media item is clicked.
 */
@Composable
fun UiMediaList(
    title: String,
    modifier: Modifier = Modifier,
    items: ImmutableList<MediaUiModel>,
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
                items(items.size) { index ->
                    UiMediaItem(items[index], onClick = onClick)
                }
            }
        }
    }
}

/**
 * A skeleton placeholder for [UiMediaList] to be used during loading states.
 *
 * @param modifier The modifier to be applied to this component.
 * @param contentPadding The padding to be applied to the content.
 * @param itemCount The number of skeleton items to display in the list.
 */
@Composable
fun UiMediaListSkeleton(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    itemCount: Int = 30
) {
    Column(modifier = modifier.padding(contentPadding)) {
        UiShimmerBox(
            modifier = Modifier
                .width(180.dp)
                .height(dimensionResource(R.dimen.spacing_8x))
                .padding(vertical = dimensionResource(R.dimen.spacing_1x))
        )
        LazyRow(
            horizontalArrangement = Arrangement
                .spacedBy(dimensionResource(R.dimen.spacing_1x))
        ) {
            items(itemCount) {
                UiMediaItemSkeleton()
            }
        }
    }
}

@UiScreenPreview
@Composable
private fun UiMediaPreview() {
    UiMediaList(
        title = "Imagined Movies",
        items = fakeMedias()
    )
}

@UiScreenPreview
@Composable
private fun UiMediaListWitchContentPaddingPreview() {
    UiMediaList(
        title = "Imagined Movies",
        contentPadding = PaddingValues(start = dimensionResource(R.dimen.spacing_4x)),
        items = fakeMedias()
    )
}

@UiScreenPreview
@Composable
private fun UiMediaListWithBackgroundPreview() {
    UiMediaList(
        title = "Imagined Movies",
        modifier = Modifier.defaultBackground(),
        items = fakeMedias()
    )
}

@UiScreenPreview
@Composable
private fun UiMediaListEmptyPreview() {
    UiMediaList(
        title = "Empty List",
        modifier = Modifier.defaultBackground(),
        items = emptyList<MediaUiModel>().toImmutableList()
    )
}

@UiScreenPreview
@Composable
private fun UiMediaListSkeletonPreview() {
    UiShimmerProvider {
        UiMediaListSkeleton(
            contentPadding = PaddingValues(dimensionResource(R.dimen.spacing_4x))
        )
    }
}
