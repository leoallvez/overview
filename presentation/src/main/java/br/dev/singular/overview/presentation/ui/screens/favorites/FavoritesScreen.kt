package br.dev.singular.overview.presentation.ui.screens.favorites

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.LazyPagingItems
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.model.MediaUiParam
import br.dev.singular.overview.presentation.model.MediaUiType
import br.dev.singular.overview.presentation.tagging.TagMediaManager
import br.dev.singular.overview.presentation.tagging.params.TagFavorites
import br.dev.singular.overview.presentation.tagging.params.TagStatus
import br.dev.singular.overview.presentation.ui.components.UiScaffold
import br.dev.singular.overview.presentation.ui.components.UiToolbar
import br.dev.singular.overview.presentation.ui.components.media.UiMediaTypeSelector
import br.dev.singular.overview.presentation.ui.screens.common.LoadingScreen
import br.dev.singular.overview.presentation.ui.screens.common.UiLifecycle
import br.dev.singular.overview.presentation.ui.screens.common.StateScreen
import br.dev.singular.overview.presentation.ui.screens.common.UiMediaContentStateView

/**
 * A composable that displays the user's favorite media.
 *
 * @param tagPath The path for analytics tagging.
 * @param isLoading Whether the screen is currently loading data.
 * @param uiParam The UI parameters for the screen, such as the selected media type.
 * @param uiPages The paginated list of favorite media items.
 * @param onReload Callback to reload the data.
 * @param onSetLoading Callback to set the loading state.
 * @param onSetType Callback to set the media type filter.
 * @param onToMediaDetails Callback to navigate to the details of a media item.
 */
@Composable
fun FavoritesScreen(
    tagPath: String = TagFavorites.PATH,
    isLoading: Boolean,
    uiParam: MediaUiParam,
    uiPages: LazyPagingItems<MediaUiModel>,
    onReload: () -> Unit,
    onSetLoading: (Boolean) -> Unit,
    onSetType: (MediaUiType) -> Unit,
    onToMediaDetails: (MediaUiModel) -> Unit,
) {
    UiScaffold(
        topBar = { UiToolbar(title = stringResource(id = R.string.favorites)) }
    ) { padding ->
        UiLifecycle(
            onPause = {
                onSetLoading(true)
            },
            onResume = {
                onReload()
                onSetLoading(false)
            }
        )
        Column(Modifier.padding(top = padding.calculateTopPadding())) {
            UiMediaTypeSelector(
                type = uiParam.type,
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.spacing_4x))
            ) { type ->
                TagMediaManager.logTypeClick(tagPath, type)
                onSetType(type)
            }
            if (isLoading) {
                LoadingScreen(tagPath, animationDelay = 0)
            } else {
                UiMediaContentStateView(
                    tagPath = tagPath,
                    pagedMedias = uiPages,
                    loadingScreen = {},
                    errorScreen = { EmptyFavoritesScreen(tagPath, uiParam.type) },
                    onClickItem = { media ->
                        TagMediaManager.logMediaClick(tagPath, media.id)
                        onToMediaDetails(media)
                    }
                )
            }
        }
    }
}

@Composable
private fun EmptyFavoritesScreen(tagPath: String, type: MediaUiType) {
    StateScreen(
        title = stringResource(
            when (type) {
                MediaUiType.MOVIE -> R.string.liked_movie_not_found
                MediaUiType.TV -> R.string.liked_tv_show_not_found
                else -> R.string.liked_not_found
            }
        ),
        tagPath = tagPath,
        tagStatus = TagStatus.NOTHING_FOUND
    )
}
