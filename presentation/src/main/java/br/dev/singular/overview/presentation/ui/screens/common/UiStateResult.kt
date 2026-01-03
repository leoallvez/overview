package br.dev.singular.overview.presentation.ui.screens.common

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.tagging.params.TagStatus

/**
 * A composable that handles the common UI states (Loading, Success, Error) and displays the
 * appropriate content for each.
 *
 * @param T The type of data expected in the success state.
 * @param uiState The current state of the UI.
 * @param tagPath The path for analytics tagging.
 * @param onRefresh A callback to be invoked when the user requests a refresh.
 * @param successContent A composable lambda to be invoked when the state is [UiState.Success],
 * providing the data of type [T].
 */
@Composable
fun <T> UiStateResult(
    uiState: UiState<T>,
    tagPath: String,
    onRefresh: () -> Unit,
    successContent: @Composable (T) -> Unit
) {
    val paddingTop = dimensionResource(R.dimen.spacing_17x)
    when (uiState) {
        is UiState.Loading -> LoadingScreen(
            tagPath,
            modifier = Modifier.padding(top = paddingTop)
        )
        is UiState.Success -> {
            TrackScreenView(tagPath, TagStatus.SUCCESS)
            successContent(uiState.data)
        }
        else -> ErrorScreen(
            tagPath,
            modifier = Modifier.padding(top = paddingTop),
            onRefresh = onRefresh
        )
    }
}
