package br.dev.singular.overview.presentation.ui.components.video

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.VideoUiModel
import br.dev.singular.overview.presentation.ui.components.shimmer.UiShimmerBox
import br.dev.singular.overview.presentation.ui.components.shimmer.UiShimmerProvider
import br.dev.singular.overview.presentation.ui.components.text.UiTitle
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview
import br.dev.singular.overview.presentation.ui.utils.fakeVideos
import br.dev.singular.overview.presentation.ui.utils.rememberMaxHeightState
import br.dev.singular.overview.presentation.ui.utils.syncMaxHeight
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

/**
 * A composable that displays a horizontal list of video items with a title.
 *
 * @param videos The immutable list of [VideoUiModel] to be displayed.
 * @param modifier The modifier to be applied to this component.
 * @param contentPadding The padding to be applied to the content.
 * @param onClick The callback to be executed when a video item is clicked.
 */
@Composable
fun UiVideoList(
    videos: ImmutableList<VideoUiModel>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onClick: (videoKey: String) -> Unit = {}
) {
    if (videos.isNotEmpty()) {
        val maxHeightState = rememberMaxHeightState()

        Column(
            modifier = modifier.animateContentSize()
        ) {
            UiTitle(
                stringResource(R.string.videos),
                modifier = Modifier.padding(contentPadding)
            )
            LazyRow(
                contentPadding = contentPadding,
                horizontalArrangement = Arrangement
                    .spacedBy(dimensionResource(R.dimen.spacing_2x))
            ) {
                items(
                    items = videos,
                    key = { it.uiId },
                    contentType = { "video" }
                ) { video ->
                    UiVideoItem(
                        video = video,
                        onClick = onClick,
                        modifier = Modifier.syncMaxHeight(maxHeightState)
                    )
                }
            }
        }
    }
}

/**
 * A skeleton placeholder for [UiVideoList] to be used during loading states.
 *
 * @param modifier The modifier to be applied to this component.
 * @param contentPadding The padding to be applied to the content.
 * @param itemCount The number of skeleton items to display in the list.
 */
@Composable
fun UiVideoListSkeleton(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    itemCount: Int = 10
) {
    Column(modifier = modifier) {
        UiShimmerBox(
            modifier = Modifier
                .padding(contentPadding)
                .width(150.dp)
                .height(dimensionResource(R.dimen.spacing_8x))
                .padding(vertical = dimensionResource(R.dimen.spacing_1x))
        )
        LazyRow(
            contentPadding = contentPadding,
            horizontalArrangement = Arrangement
                .spacedBy(dimensionResource(R.dimen.spacing_2x))
        ) {
            items(itemCount) {
                UiVideoItemSkeleton()
            }
        }
    }
}

@UiComponentPreview
@Composable
internal fun UiVideoListPreview() {
    UiVideoList(videos = fakeVideos())
}

@UiComponentPreview
@Composable
internal fun UiVideoListWithContentPaddingPreview() {
    UiVideoList(
        videos = fakeVideos(),
        contentPadding = PaddingValues(dimensionResource(R.dimen.spacing_4x))
    )
}

@UiComponentPreview
@Composable
internal fun UiVideoListEmptyPreview() {
    UiVideoList(
        videos = emptyList<VideoUiModel>().toImmutableList()
    )
}

@UiComponentPreview
@Composable
internal fun UiVideoListSkeletonPreview() {
    UiShimmerProvider {
        UiVideoListSkeleton()
    }
}
