package br.dev.singular.overview.util

import androidx.navigation.NavBackStackEntry
import br.dev.singular.overview.presentation.ui.navigation.Destination

fun NavBackStackEntry.getParams(): Pair<Long, String> {
    val id = arguments?.getLong(Destination.ID_PARAM)
    val type = arguments?.getString(Destination.TYPE_PARAM)
    return Pair(id ?: 0, type ?: "")
}

fun NavBackStackEntry.getApiId(): Long = arguments?.getLong(Destination.ID_PARAM) ?: 0

