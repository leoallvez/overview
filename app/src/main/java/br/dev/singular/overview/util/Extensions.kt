package br.dev.singular.overview.util

import androidx.navigation.NavBackStackEntry
import br.dev.singular.overview.data.source.DataResult
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.ui.navigation.Destination

fun NavBackStackEntry.getParams(): Pair<Long, String> {
    val id = arguments?.getLong(Destination.ID_PARAM)
    val type = arguments?.getString(Destination.TYPE_PARAM)
    return Pair(id ?: 0, type ?: "")
}

fun NavBackStackEntry.getApiId(): Long = arguments?.getLong(Destination.ID_PARAM) ?: 0

fun <T> DataResult<out T>.toUiState(): UiState<T?> {
    val isSuccess = this is DataResult.Success
    return if (isSuccess) UiState.Success(this.data) else UiState.Error()
}

