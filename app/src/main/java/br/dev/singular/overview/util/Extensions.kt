package br.dev.singular.overview.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources.NotFoundException
import androidx.navigation.NavBackStackEntry
import br.dev.singular.overview.data.source.DataResult
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.ui.navigation.Destination

@SuppressLint("DiscouragedApi")
fun Context.getStringByName(resource: String): String? = try {
    val resourceId = resources.getIdentifier(resource, "string", packageName)
    getString(resourceId)
} catch (e: NotFoundException) {
    null
}

fun NavBackStackEntry.getParams(): Pair<Long, String> {
    val id = arguments?.getLong(Destination.ID_PARAM)
    val type = arguments?.getString(Destination.TYPE_PARAM)
    return Pair(id ?: 0, type ?: "")
}

fun NavBackStackEntry.getApiId(): Long = arguments?.getLong(Destination.ID_PARAM) ?: 0

fun List<Long>.joinToStringWithPipe() = joinToString(separator = "|") { it.toString() }

fun <T> DataResult<out T>.toUiState(): UiState<T?> {
    val isSuccess = this is DataResult.Success
    return if (isSuccess) UiState.Success(this.data) else UiState.Error()
}

