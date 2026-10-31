package br.dev.singular.overview.presentation.ui.components.episodes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.EpisodesProgressUiModel
import br.dev.singular.overview.presentation.model.EpisodesSummaryUiModel
import br.dev.singular.overview.presentation.ui.components.card.UiCardContainer
import br.dev.singular.overview.presentation.ui.components.card.UiIconIndicator
import br.dev.singular.overview.presentation.ui.components.icon.UiIcon
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.progress.UiProgressBar
import br.dev.singular.overview.presentation.ui.components.shimmer.UiShimmerBox
import br.dev.singular.overview.presentation.ui.components.text.UiText
import br.dev.singular.overview.presentation.ui.components.text.UiTextSkeleton
import br.dev.singular.overview.presentation.ui.theme.Background
import br.dev.singular.overview.presentation.ui.theme.DefaultTextColor
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import br.dev.singular.overview.presentation.ui.theme.LowlightColor
import br.dev.singular.overview.presentation.ui.theme.SubtitleTextColor
import br.dev.singular.overview.presentation.ui.theme.SurfaceVariant
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview
import br.dev.singular.overview.presentation.ui.utils.fakeEpisodesSummary
import com.composables.icons.lucide.Calendar
import com.composables.icons.lucide.Check
import com.composables.icons.lucide.ChevronRight
import com.composables.icons.lucide.List
import com.composables.icons.lucide.Lock
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Play

/**
 * A composable that displays a navigation card to the episode list of a TV show, combining the
 * show data with the user's watching progress.
 *
 * @param model The [EpisodesSummaryUiModel] providing seasons, episodes and release data.
 * @param progress The [EpisodesProgressUiModel] with the user's progress on the show.
 * @param modifier The modifier to be applied to this component.
 * @param onClick Callback invoked when the card is clicked. Ignored while the show is not released.
 */
@Composable
internal fun UiEpisodesCard(
    model: EpisodesSummaryUiModel,
    progress: EpisodesProgressUiModel,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    if (progress is EpisodesProgressUiModel.Loading) {
        UiEpisodesCardSkeleton(modifier = modifier)
    } else {

        val isReleased = model.isReleased

        EpisodesCardContainer(
            modifier = modifier,
            enabled = isReleased,
            watched = progress.watched,
            onClick = onClick
        ) {
            EpisodesIndicator(isReleased = isReleased, progress = progress)

            EpisodesTexts(
                model = model,
                progress = progress,
                modifier = Modifier.weight(1f)
            )

            if (isReleased) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_1x))
                ) {
                    progress.watched?.let { WatchedBadge(progress = it) }
                    UiIcon(
                        source = UiIconSource.vector(Lucide.ChevronRight),
                        color = SubtitleTextColor,
                        modifier = Modifier.size(dimensionResource(R.dimen.spacing_6x))
                    )
                }
            }
        }
    }
}

/**
 * A skeleton loader for [UiEpisodesCard].
 *
 * @param modifier The modifier to be applied to this component.
 */
@Composable
internal fun UiEpisodesCardSkeleton(
    modifier: Modifier = Modifier
) {
    EpisodesCardContainer(
        modifier = modifier,
        enabled = false
    ) {
        UiShimmerBox(
            modifier = Modifier.size(dimensionResource(R.dimen.spacing_12x)),
            shape = CircleShape
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement
                .spacedBy(dimensionResource(R.dimen.spacing_2x))
        ) {
            UiTextSkeleton()
            UiTextSkeleton(
                modifier = Modifier.fillMaxWidth(SKELETON_SHORT_LINE_FRACTION)
            )
        }
    }
}

@Composable
private fun EpisodesCardContainer(
    enabled: Boolean,
    modifier: Modifier = Modifier,
    watched: EpisodesProgressUiModel.InProgress? = null,
    onClick: () -> Unit = {},
    content: @Composable RowScope.() -> Unit
) {
    UiCardContainer(
        modifier = modifier,
        enabled = enabled,
        contentPadding = PaddingValues(),
        onClick = onClick
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = dimensionResource(R.dimen.spacing_5x),
                        vertical = dimensionResource(R.dimen.spacing_4x)
                    )
                    .heightIn(min = dimensionResource(R.dimen.spacing_12x)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement
                    .spacedBy(dimensionResource(R.dimen.spacing_4x)),
                content = content
            )
            watched?.let {
                UiProgressBar(
                    progress = it.progress,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}

@Composable
private fun EpisodesIndicator(
    isReleased: Boolean,
    progress: EpisodesProgressUiModel
) {
    when {
        !isReleased -> UiIconIndicator(
            icon = Lucide.Calendar,
            sizeRes = R.dimen.spacing_12x,
            iconSizeRes = R.dimen.spacing_6x,
            iconColor = LowlightColor,
            containerColor = SurfaceVariant
        )

        progress is EpisodesProgressUiModel.Completed -> UiIconIndicator(
            icon = Lucide.Check,
            sizeRes = R.dimen.spacing_12x,
            iconSizeRes = R.dimen.spacing_6x,
            iconColor = Background,
            containerColor = HighlightColor
        )

        else -> UiIconIndicator(
            icon = if (progress is EpisodesProgressUiModel.InProgress) Lucide.Play else Lucide.List,
            sizeRes = R.dimen.spacing_12x,
            iconSizeRes = R.dimen.spacing_6x
        )
    }
}

@Composable
private fun EpisodesTexts(
    model: EpisodesSummaryUiModel,
    progress: EpisodesProgressUiModel,
    modifier: Modifier = Modifier
) {
    val isReleased = model.isReleased

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_1x))
    ) {
        CardTitle(
            text = stringResource(R.string.episodes_title),
            color = if (isReleased) DefaultTextColor else SubtitleTextColor
        )
        CardSubtitle(
            text = if (isReleased) {
                seasonsAndEpisodes(model)
            } else {
                stringResource(R.string.episodes_premieres, model.releaseDate)
            },
            color = if (isReleased) SubtitleTextColor else LowlightColor
        )
        if (isReleased && progress is EpisodesProgressUiModel.SignedOut) {
            SignInHint()
        }
    }
}

@Composable
private fun WatchedBadge(
    progress: EpisodesProgressUiModel.InProgress
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(dimensionResource(R.dimen.spacing_6x))
            .background(
                color = HighlightColor.copy(alpha = BADGE_CONTAINER_ALPHA),
                shape = CircleShape
            )
            .padding(horizontal = dimensionResource(R.dimen.spacing_2x))
    ) {
        UiText(
            text = "${progress.percentage}%",
            style = MaterialTheme.typography.labelMedium,
            color = HighlightColor,
            isBold = true,
            maxLines = 1
        )
    }
}

@Composable
private fun CardTitle(
    text: String,
    color: Color = DefaultTextColor
) {
    UiText(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = color,
        textAlign = TextAlign.Start,
        isBold = true,
        maxLines = 1
    )
}

@Composable
private fun CardSubtitle(
    text: String,
    color: Color = SubtitleTextColor
) {
    UiText(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = color,
        textAlign = TextAlign.Start,
        maxLines = 1
    )
}

@Composable
private fun SignInHint() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_1x))
    ) {
        UiIcon(
            source = UiIconSource.vector(Lucide.Lock),
            color = LowlightColor,
            modifier = Modifier.size(dimensionResource(R.dimen.spacing_3x))
        )
        UiText(
            text = stringResource(R.string.episodes_sign_in_hint),
            style = MaterialTheme.typography.bodySmall,
            color = LowlightColor,
            textAlign = TextAlign.Start,
            maxLines = 1
        )
    }
}

@UiComponentPreview
@Composable
internal fun UiEpisodesCardDefaultPreview() {
    UiEpisodesCard(
        model = fakeEpisodesSummary(),
        progress = EpisodesProgressUiModel.NotStarted,
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_4x))
    )
}

@UiComponentPreview
@Composable
internal fun UiEpisodesCardInProgressPreview() {
    UiEpisodesCard(
        model = fakeEpisodesSummary(),
        progress = EpisodesProgressUiModel.InProgress(progress = 0.08f),
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_4x))
    )
}

@UiComponentPreview
@Composable
internal fun UiEpisodesCardCompletedPreview() {
    UiEpisodesCard(
        model = fakeEpisodesSummary(),
        progress = EpisodesProgressUiModel.Completed,
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_4x))
    )
}

@UiComponentPreview
@Composable
internal fun UiEpisodesCardSignedOutPreview() {
    UiEpisodesCard(
        model = fakeEpisodesSummary(),
        progress = EpisodesProgressUiModel.SignedOut,
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_4x))
    )
}

@UiComponentPreview
@Composable
internal fun UiEpisodesCardNotReleasedPreview() {
    UiEpisodesCard(
        model = fakeEpisodesSummary().copy(isReleased = false, releaseDate = "Mar 12, 2027"),
        progress = EpisodesProgressUiModel.NotStarted,
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_4x))
    )
}

@UiComponentPreview
@Composable
internal fun UiEpisodesCardLoadingPreview() {
    UiEpisodesCard(
        model = fakeEpisodesSummary(),
        progress = EpisodesProgressUiModel.Loading,
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_4x))
    )
}

@UiComponentPreview
@Composable
internal fun UiEpisodesCardSkeletonPreview() {
    UiEpisodesCardSkeleton(
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_4x))
    )
}
