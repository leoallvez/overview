package br.dev.singular.overview.presentation.ui.screens.common

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.tagging.params.TagStatus
import br.dev.singular.overview.presentation.ui.components.UiCenteredColumn
import br.dev.singular.overview.presentation.ui.components.UiChip
import br.dev.singular.overview.presentation.ui.components.UiIcon
import br.dev.singular.overview.presentation.ui.components.text.UiText
import br.dev.singular.overview.presentation.ui.components.text.UiTitle
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import com.ehsanmsz.mszprogressindicator.progressindicator.BallScaleRippleMultipleProgressIndicator
import android.R as X


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

@Composable
fun LoadingScreen(
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

@Preview
@Composable
fun LoadingScreenPreview() {
    LoadingScreen("tag")
}

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
                        icon = Icons.Filled.Refresh,
                        color = HighlightColor
                    )
                }
            )
        },
    )
}

@Preview
@Composable
fun ErrorScreenPreview() {
    ErrorScreen("tag") {}
}

@Composable
fun NothingFoundScreen(tagPath: String, modifier: Modifier = Modifier, hasFilters: Boolean = false) {
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

@Preview
@Composable
fun NothingFoundScreenPreview() {
    NothingFoundScreen("tag")
}

@Preview
@Composable
fun NothingFoundScreenWithFilterPreview() {
    NothingFoundScreen("tag", hasFilters = true)
}
