package br.dev.singular.overview.presentation.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import br.dev.singular.overview.presentation.model.ScrollUiState
import br.dev.singular.overview.presentation.ui.components.style.UiBorderStyle
import br.dev.singular.overview.presentation.ui.theme.Background
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun Modifier.border(style: UiBorderStyle = UiBorderStyle()): Modifier = with(style) {
    return if (visible) {
        border(dimensionResource(width), color, shape)
    } else {
        this@border
    }
}

@Composable
fun Modifier.defaultBackground() = background(Background)

@Composable
fun rememberLazyGridScrollState(
    state: ScrollUiState,
    onSet: (ScrollUiState) -> Unit
): LazyGridState {
    return rememberLazyGridState(
        initialFirstVisibleItemIndex = state.index,
        initialFirstVisibleItemScrollOffset = state.offset
    ).apply {
        OnSetScrollState(onSet = onSet)
    }
}

@Composable
fun LazyGridState.OnSetScrollState(
    onSet: (state: ScrollUiState) -> Unit
) {
    LaunchedEffect(this) {
        snapshotFlow {
            firstVisibleItemIndex to firstVisibleItemScrollOffset
        }
            .distinctUntilChanged()
            .collect { (index, offset) ->
                onSet(ScrollUiState(index, offset))
            }
    }
}
