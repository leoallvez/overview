package br.dev.singular.overview.presentation.ui.screens.media

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.model.MediaDetailsUiModel
import br.dev.singular.overview.presentation.ui.screens.common.ErrorScreen
import br.dev.singular.overview.presentation.ui.screens.common.UiStateResult

/**
 * A specialized version of [UiStateResult] for Media Details screens.
 * It handles the [MediaDetailsSkeletonScreen] and ensures the data is not null
 * before calling the [content] lambda.
 */
@Composable
internal fun <T : MediaDetailsUiModel> MediaUiStateResult(
    id: Long,
    tagPath: String,
    onLoad: (Long) -> Unit,
    uiState: UiState<T?>,
    content: @Composable (T) -> Unit
) {
    val onRefresh = { onLoad(id) }

    LaunchedEffect(Unit) { onRefresh() }

    UiStateResult(
        uiState = uiState,
        tagPath = tagPath,
        onRefresh = onRefresh,
        loadingContent = {
            MediaDetailsSkeletonScreen(tagPath = tagPath)
        }
    ) { data ->
        if (data == null) {
            ErrorScreen(tagPath = tagPath, onRefresh = onRefresh)
        } else {
            content(data)
        }
    }
}
