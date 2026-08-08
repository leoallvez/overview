package br.dev.singular.overview.presentation.ui.components.video

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.VideoUiModel
import br.dev.singular.overview.presentation.ui.components.UiImage
import br.dev.singular.overview.presentation.ui.components.icon.UiIconButton
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconStyle
import br.dev.singular.overview.presentation.ui.components.shimmer.UiShimmerBox
import br.dev.singular.overview.presentation.ui.components.shimmer.UiShimmerProvider
import br.dev.singular.overview.presentation.ui.components.style.UiBorderStyle
import br.dev.singular.overview.presentation.ui.components.style.UiImageStyle
import br.dev.singular.overview.presentation.ui.components.text.UiText
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import br.dev.singular.overview.presentation.ui.theme.Surface
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview
import br.dev.singular.overview.presentation.ui.utils.fakeVideo
import br.dev.singular.overview.presentation.ui.utils.floatResource
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Play

/**
 * A composable that displays a video item with a thumbnail, a play icon, and a name.
 *
 * @param video The [VideoUiModel] containing the video data to be displayed.
 * @param modifier The modifier to be applied to this component.
 * @param onClick The callback to be executed when the video item or the play button is clicked.
 */
@Composable
fun UiVideoItem(
    video: VideoUiModel,
    modifier: Modifier = Modifier,
    onClick: (videoKey: String) -> Unit = {}
) {
    val iconAlpha = floatResource(R.dimen.thumbnail_icon_alpha)
    val shape = RoundedCornerShape(size = dimensionResource(R.dimen.corner_width))

    Column(
        modifier = modifier
            .clickable { onClick(video.key) }
            .semantics(mergeDescendants = true) {}
    ) {
        Box(
            modifier = Modifier
                .width(dimensionResource(R.dimen.thumbnail_width))
                .aspectRatio(16f / 9f)
        ) {
            UiImage(
                modifier = Modifier.fillMaxSize(),
                url = video.thumbnailURL,
                contentDescription = video.name,
                style = UiImageStyle(
                    contentScale = ContentScale.Crop,
                    shape = shape,
                    previewDrawableRes = video.previewDrawableRes,
                    borderStyle = UiBorderStyle(shape = shape)
                )
            )
            UiIconButton(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(color = Surface.copy(alpha = iconAlpha))
                    .align(Alignment.Center),
                onClick = { onClick(video.key) },
                iconStyle = UiIconStyle(
                    source = UiIconSource.vector(icon = Lucide.Play),
                    color = HighlightColor.copy(alpha = iconAlpha),
                    sizeRes = R.dimen.spacing_5x
                ),
                borderStyle = UiBorderStyle(
                    color = HighlightColor.copy(alpha = iconAlpha)
                )
            )
        }
        UiText(
            text = video.name,
            modifier = Modifier
                .width(dimensionResource(R.dimen.thumbnail_width))
                .padding(top = dimensionResource(R.dimen.spacing_1x)),
            style = MaterialTheme.typography.bodyMedium,
            isBold = true,
            textAlign = TextAlign.Start,
            maxLines = 2
        )
    }
}

/**
 * A skeleton placeholder for [UiVideoItem] to be used during loading states.
 *
 * @param modifier The modifier to be applied to this component.
 */
@Composable
fun UiVideoItemSkeleton(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        UiShimmerBox(
            modifier = Modifier
                .width(dimensionResource(R.dimen.thumbnail_width))
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(size = dimensionResource(R.dimen.corner_width)))
        )
        UiShimmerBox(
            modifier = Modifier
                .padding(top = dimensionResource(R.dimen.spacing_1x))
                .width(dimensionResource(R.dimen.thumbnail_width))
                .height(20.dp)
        )
    }
}

@UiComponentPreview
@Composable
internal fun UiVideoItemPreview() {
    UiVideoItem(
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_3x)),
        video = fakeVideo(
            name = stringResource(R.string.lorem_ipsum_long)
        )
    )
}

@UiComponentPreview
@Composable
internal fun UiVideoItemSkeletonPreview() {
    UiShimmerProvider {
        UiVideoItemSkeleton(
            modifier = Modifier.padding(dimensionResource(R.dimen.spacing_3x))
        )
    }
}
