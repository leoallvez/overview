package br.dev.singular.overview.presentation.ui.screens.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.tagging.params.TagStatus
import br.dev.singular.overview.presentation.ui.components.UiCenteredColumn
import br.dev.singular.overview.presentation.ui.components.UiChip
import br.dev.singular.overview.presentation.ui.components.icon.UiIcon
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.media.UiMediaGridSkeleton
import br.dev.singular.overview.presentation.ui.components.media.UiMediaListSkeleton
import br.dev.singular.overview.presentation.ui.components.shimmer.UiItemSkeleton
import br.dev.singular.overview.presentation.ui.components.text.UiText
import br.dev.singular.overview.presentation.ui.components.text.UiTitle
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import br.dev.singular.overview.presentation.ui.utils.UiScreenPreview
import com.ehsanmsz.mszprogressindicator.progressindicator.BallScaleRippleMultipleProgressIndicator
import android.R as X

/**
 * A composable that displays a generic state screen with a title and optional secondary content.
 * It also handles analytics screen view tracking.
 *
 * @param title The main title to be displayed on the screen.
 * @param tagPath The path for analytics tagging.
 * @param tagStatus The status for analytics tagging.
 * @param modifier The modifier to be applied to the root element.
 * @param secondaryContent A composable lambda for displaying content below the title.
 */
@Composable
fun StateScreen(
    title: String,
    tagPath: String,
    tagStatus: String,
    modifier: Modifier = Modifier,
    secondaryContent: @Composable ColumnScope.() -> Unit = {}
) {
    TrackScreenView(tagPath, tagStatus)
    UiCenteredColumn(modifier) {
        UiTitle(
            text = title,
            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.spacing_2x)),
            color = HighlightColor,
            textAlign = TextAlign.Center
        )
        secondaryContent()
    }
}

/**
 * A composable that displays a loading indicator screen.
 *
 * @param tagPath The path for analytics tagging.
 * @param modifier The modifier to be applied to the root element.
 * @param animationDelay The delay in milliseconds before the animation starts.
 */
@Composable
fun LoadingProgressScreen(
    tagPath: String,
    modifier: Modifier = Modifier,
    animationDelay: Int = 400
) {
    StateScreen(
        title = stringResource(R.string.loading),
        modifier = modifier,
        tagPath = tagPath,
        tagStatus = TagStatus.LOADING,
        secondaryContent = {
            BallScaleRippleMultipleProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = HighlightColor,
                animationDelay = animationDelay,
                animationDuration = integerResource(X.integer.config_longAnimTime)
            )
        }
    )
}

@UiScreenPreview
@Composable
private fun LoadingScreenPreview() {
    LoadingProgressScreen("tag")
}

/**
 * A composable that displays an error message with a "try again" button.
 *
 * @param tagPath The path for analytics tagging.
 * @param modifier The modifier to be applied to the root element.
 * @param onRefresh A callback to be invoked when the "try again" button is clicked.
 */
@Composable
fun ErrorScreen(tagPath: String, modifier: Modifier = Modifier, onRefresh: () -> Unit) {
    StateScreen(
        title = stringResource(R.string.error_on_loading),
        modifier = modifier,
        tagPath = tagPath,
        tagStatus = TagStatus.ERROR,
        secondaryContent = {
            UiChip(
                text = stringResource(R.string.btn_try_again),
                activated = true,
                onClick = onRefresh,
                icon = {
                    UiIcon(
                        source = UiIconSource.vector(Icons.Filled.Refresh),
                        color = HighlightColor
                    )
                }
            )
        },
    )
}

@UiScreenPreview
@Composable
private fun ErrorScreenPreview() {
    ErrorScreen("tag") {}
}

/**
 * A composable that displays a "nothing found" message.
 *
 * @param tagPath The path for analytics tagging.
 * @param modifier The modifier to be applied to the root element.
 * @param hasFilters A boolean indicating whether to show a message about checking filters.
 */
@Composable
fun NothingFoundScreen(
    tagPath: String,
    modifier: Modifier = Modifier,
    hasFilters: Boolean = false
) {
    StateScreen(
        title = stringResource(R.string.not_found),
        modifier = modifier,
        tagPath = tagPath,
        tagStatus = TagStatus.NOTHING_FOUND,
        secondaryContent = {
            if (hasFilters) {
                UiText(stringResource(id = R.string.check_filters))
            }
        }
    )
}

@UiScreenPreview
@Composable
private fun NothingFoundScreenPreview() {
    NothingFoundScreen("tag")
}

@UiScreenPreview
@Composable
private fun NothingFoundScreenWithFilterPreview() {
    NothingFoundScreen("tag", hasFilters = true)
}

/**
 * A composable that displays a skeleton screen for a media grid while content is loading.
 * It also handles analytics screen view tracking.
 *
 * @param tagPath The path for analytics tagging.
 * @param modifier The modifier to be applied to the root element.
 */
@Composable
fun MediaGridSkeletonScreen(
    tagPath: String,
    modifier: Modifier = Modifier
) {
    TrackScreenView(tagPath)
    UiMediaGridSkeleton(modifier = modifier)
}

@UiScreenPreview
@Composable
private fun MediaGridSkeletonScreenPreview() {
    MediaGridSkeletonScreen("tag")
}


@Composable
fun MediaListSkeletonScreen(
    tagPath: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    TrackScreenView(tagPath)
    LazyColumn(modifier = modifier) {
        items(10) {
            UiMediaListSkeleton(
                contentPadding = contentPadding,
                modifier = modifier
            )
        }
    }
}

@UiScreenPreview
@Composable
private fun MediaListSkeletonScreenPreview() {
    MediaListSkeletonScreen("tag")
}

@Composable
internal fun ItemListSkeletonScreen(
    tagPath: String,
    modifier: Modifier = Modifier,
) {
    TrackScreenView(tagPath)
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement
            .spacedBy(dimensionResource(R.dimen.spacing_2x)),
    ) {
        items(30) {
            UiItemSkeleton()
        }
    }
}

@UiScreenPreview
@Composable
private fun ItemListSkeletonScreenPreview() {
    ItemListSkeletonScreen("tag")
}
