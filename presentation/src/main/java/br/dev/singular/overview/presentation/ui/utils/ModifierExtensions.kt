package br.dev.singular.overview.presentation.ui.utils

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import br.dev.singular.overview.presentation.model.ScrollUiState
import br.dev.singular.overview.presentation.ui.components.style.UiBorderStyle
import br.dev.singular.overview.presentation.ui.theme.Background
import kotlinx.coroutines.flow.distinctUntilChanged

@Stable
class MaxHeightState {
    var maxHeightPx by mutableIntStateOf(0)
}

@Composable
fun rememberMaxHeightState(): MaxHeightState {
    return remember { MaxHeightState() }
}

@Composable
fun Modifier.syncMaxHeight(state: MaxHeightState): Modifier {
    val density = LocalDensity.current
    return this
        .onGloballyPositioned { coordinates ->
            if (coordinates.size.height > state.maxHeightPx) {
                state.maxHeightPx = coordinates.size.height
            }
        }
        .then(
            if (state.maxHeightPx > 0) {
                Modifier
                    .height(with(density) { state.maxHeightPx.toDp() })
                    .animateContentSize()
            } else {
                Modifier
            }
        )
}

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
