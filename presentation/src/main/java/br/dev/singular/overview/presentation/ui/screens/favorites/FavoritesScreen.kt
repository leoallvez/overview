package br.dev.singular.overview.presentation.ui.screens.favorites

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.MediaUiModel
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

@Composable
fun FavoritesScreen(
    tagPath: String = TagFavorites.PATH,
    onMediaClick: (MediaUiModel) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val uiParam = viewModel.uiParam.collectAsState().value
    val pagedMedias = viewModel.medias.collectAsLazyPagingItems()

    UiScaffold(
        topBar = { UiToolbar(title = stringResource(id = R.string.favorites)) }
    ) { padding ->
        UiLifecycle(
            onPause = {
                viewModel.onLoadingChanged(true)
            },
            onResume = {
                viewModel.onReload()
                viewModel.onLoadingChanged(false)
            }
        )
        Column(Modifier.padding(top = padding.calculateTopPadding())) {
            UiMediaTypeSelector(
                type = uiParam.type,
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.spacing_4x))
            ) { type ->
                TagMediaManager.logTypeClick(tagPath, type)
                viewModel.onSelectType(type)
            }
            if (viewModel.isLoading) {
                LoadingScreen(tagPath, animationDelay = 0)
            } else {
                UiMediaContentStateView(
                    tagPath = tagPath,
                    pagedMedias = pagedMedias,
                    loadingScreen = {},
                    errorScreen = { EmptyFavoritesScreen(tagPath, uiParam.type) },
                    onClickItem = { media ->
                        TagMediaManager.logMediaClick(tagPath, media.id)
                        onMediaClick(media)
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
