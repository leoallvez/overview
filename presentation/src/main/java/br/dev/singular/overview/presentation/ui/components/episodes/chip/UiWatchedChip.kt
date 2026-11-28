package br.dev.singular.overview.presentation.ui.components.episodes.chip

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.UiChip
import br.dev.singular.overview.presentation.ui.components.icon.UiIcon
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.shimmer.UiShimmerBox
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import br.dev.singular.overview.presentation.ui.theme.LowlightColor
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview
import com.composables.icons.lucide.Check
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Plus

/**
 * A specialized chip to indicate and toggle the "Watched" state of an episode or media.
 *
 * @param isWatched Whether the media is currently marked as watched.
 * @param modifier The modifier to be applied to the chip.
 * @param onClick Callback invoked when the chip is clicked.
 */
@Composable
internal fun UiWatchedChip(
    isWatched: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    UiChip(
        text = stringResource(if (isWatched) R.string.episodes_watched else R.string.episodes_mark),
        activated = isWatched,
        modifier = modifier,
        icon = {
            UiIcon(
                source = UiIconSource.vector(if (isWatched) Lucide.Check else Lucide.Plus),
                color = if (isWatched) HighlightColor else LowlightColor,
                modifier = Modifier.size(dimensionResource(R.dimen.spacing_4x))
            )
        },
        onClick = onClick
    )
}

/**
 * A skeleton loader for [UiWatchedChip].
 *
 * @param modifier The modifier to be applied to the skeleton.
 */
@Composable
internal fun UiWatchedChipSkeleton(
    modifier: Modifier = Modifier
) {
    UiShimmerBox(
        modifier = modifier
            .width(dimensionResource(R.dimen.spacing_20x))
            .height(dimensionResource(R.dimen.spacing_7x))
    )
}

@UiComponentPreview
@Composable
internal fun UiWatchedChipWatchedPreview() {
    UiWatchedChip(
        isWatched = true,
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_2x))
    )
}

@UiComponentPreview
@Composable
internal fun UiWatchedChipMarkPreview() {
    UiWatchedChip(
        isWatched = false,
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_2x))
    )
}

@UiComponentPreview
@Composable
internal fun UiWatchedChipSkeletonPreview() {
    UiWatchedChipSkeleton(
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_2x))
    )
}
