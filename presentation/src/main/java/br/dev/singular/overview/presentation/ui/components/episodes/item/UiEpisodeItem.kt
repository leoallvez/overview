package br.dev.singular.overview.presentation.ui.components.episodes.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.EpisodeUiModel
import br.dev.singular.overview.presentation.ui.components.UiImage
import br.dev.singular.overview.presentation.ui.components.card.UiCardContainer
import br.dev.singular.overview.presentation.ui.components.episodes.chip.UiWatchedChip
import br.dev.singular.overview.presentation.ui.components.episodes.chip.UiWatchedChipSkeleton
import br.dev.singular.overview.presentation.ui.components.icon.UiIcon
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.shimmer.UiShimmerBox
import br.dev.singular.overview.presentation.ui.components.style.UiBorderStyle
import br.dev.singular.overview.presentation.ui.components.style.UiImageStyle
import br.dev.singular.overview.presentation.ui.components.text.UiText
import br.dev.singular.overview.presentation.ui.components.text.UiTextSkeleton
import br.dev.singular.overview.presentation.ui.theme.DefaultTextColor
import br.dev.singular.overview.presentation.ui.theme.SubtitleTextColor
import br.dev.singular.overview.presentation.ui.theme.Surface
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview
import br.dev.singular.overview.presentation.ui.utils.fakeEpisode
import com.composables.icons.lucide.ChevronRight
import com.composables.icons.lucide.Lucide

/**
 * A composable that displays a single episode item.
 *
 * @param model The data representing the episode details.
 * @param modifier The modifier to be applied to this component.
 * @param onClick Callback invoked when the whole item is clicked.
 * @param onToggleWatched Callback invoked when the "Watched" / "Mark" button is clicked.
 */
@Composable
internal fun UiEpisodeItem(
    model: EpisodeUiModel,
    isWatched: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onToggleWatched: () -> Unit = {}
) {
    UiCardContainer(
        modifier = modifier,
        contentPadding = PaddingValues(dimensionResource(R.dimen.spacing_2x)),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_4x))
        ) {
            EpisodeImage(model = model)

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_2x))
            ) {
                UiText(
                    text = model.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = DefaultTextColor,
                    textAlign = TextAlign.Start,
                    isBold = true,
                    maxLines = 2
                )
                
                UiWatchedChip(
                    isWatched = isWatched,
                    onClick = onToggleWatched
                )
            }

            UiIcon(
                source = UiIconSource.vector(Lucide.ChevronRight),
                color = SubtitleTextColor,
                modifier = Modifier
                    .padding(end = dimensionResource(R.dimen.spacing_2x))
                    .size(dimensionResource(R.dimen.spacing_6x))
            )
        }
    }
}

@Composable
private fun EpisodeImage(
    model: EpisodeUiModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(dimensionResource(R.dimen.poster_width))
            .height(dimensionResource(R.dimen.spacing_18x))
    ) {
        UiImage(
            url = model.imageURL,
            modifier = Modifier.matchParentSize(),
            style = UiImageStyle(
                previewDrawableRes = model.previewDrawableRes,
                borderStyle = UiBorderStyle(
                    shape = RoundedCornerShape(dimensionResource(R.dimen.corner_width))
                )
            )
        )
        
        EpisodeBadge(
            text = stringResource(R.string.episode_badge, model.episodeNumber),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(dimensionResource(R.dimen.spacing_1x))
        )

        EpisodeBadge(
            text = model.duration,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(dimensionResource(R.dimen.spacing_1x))
        )
    }
}

@Composable
private fun EpisodeBadge(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = Surface.copy(alpha = 0.7f),
                shape = RoundedCornerShape(dimensionResource(R.dimen.spacing_1x))
            )
            .padding(horizontal = dimensionResource(R.dimen.spacing_1x))
    ) {
        UiText(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = DefaultTextColor,
            isBold = true,
            maxLines = 1
        )
    }
}

/**
 * A skeleton loader for [UiEpisodeItem].
 *
 * @param modifier The modifier to be applied to this component.
 */
@Composable
internal fun UiEpisodeItemSkeleton(
    modifier: Modifier = Modifier
) {
    UiCardContainer(
        modifier = modifier,
        enabled = false,
        contentPadding = PaddingValues(dimensionResource(R.dimen.spacing_2x))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_4x))
        ) {
            UiShimmerBox(
                modifier = Modifier
                    .width(dimensionResource(R.dimen.poster_width))
                    .height(dimensionResource(R.dimen.spacing_18x)),
                shape = RoundedCornerShape(dimensionResource(R.dimen.corner_width))
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_2x))
            ) {
                UiTextSkeleton()
                UiWatchedChipSkeleton()
            }
        }
    }
}

@UiComponentPreview
@Composable
internal fun UiEpisodeItemWatchedPreview() {
    UiEpisodeItem(
        model = fakeEpisode(),
        isWatched = true,
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_4x))
    )
}

@UiComponentPreview
@Composable
internal fun UiEpisodeItemUnwatchedPreview() {
    UiEpisodeItem(
        model = fakeEpisode(),
        isWatched = false,
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_4x))
    )
}

@UiComponentPreview
@Composable
internal fun UiEpisodeItemSkeletonPreview() {
    UiEpisodeItemSkeleton(
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_4x))
    )
}
