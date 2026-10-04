package br.dev.singular.overview.presentation.ui.components.snackbar

import androidx.compose.runtime.mutableStateListOf
import br.dev.singular.overview.presentation.ui.components.snackbar.UiSnackbarHostState.Companion.MAX_VISIBLE_SNACKBARS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Global state holder for managing snackbar notifications across the application.
 *
 * This class is a Singleton managed by Hilt, allowing ViewModels and UI components
 * to trigger snackbar messages from anywhere in the app. It maintains a reactive list
 * of active snackbars with support for visual stacking (up to [MAX_VISIBLE_SNACKBARS])
 * and thread-safe operations.
 */
@Singleton
class UiSnackbarHostState @Inject constructor() {

    /**
     * A reactive list of currently active snackbar visuals displayed by [UiSnackbarHost].
     */
    val snackbarList: List<UiSnackbarVisuals>
        field = mutableStateListOf<UiSnackbarVisuals>()

    private fun addSnackbar(visuals: UiSnackbarVisuals) {
        if (snackbarList.size >= MAX_VISIBLE_SNACKBARS) {
            snackbarList.removeAt(0)
        }
        snackbarList.add(visuals)
    }


    /**
     * Enqueues and displays a snackbar with the given [visuals].
     * If the number of active snackbars reaches [MAX_VISIBLE_SNACKBARS], the oldest
     * snackbar is removed to make room.
     *
     * Ensures thread-safe execution on the main thread.
     *
     * @param visuals The [UiSnackbarVisuals] configuration ([UiSnackbarVisuals.Close] or [UiSnackbarVisuals.Action]).
     */
    fun show(visuals: UiSnackbarVisuals) {
        if (android.os.Looper.myLooper() == android.os.Looper.getMainLooper()) {
            addSnackbar(visuals)
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                addSnackbar(visuals)
            }
        }
    }

    /**
     * Dismisses and removes a specific [visuals] snackbar from the active list.
     *
     * Ensures thread-safe execution on the main thread.
     *
     * @param visuals The [UiSnackbarVisuals] to be dismissed.
     */
    fun dismiss(visuals: UiSnackbarVisuals) {
        if (android.os.Looper.myLooper() == android.os.Looper.getMainLooper()) {
            snackbarList.remove(visuals)
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                snackbarList.remove(visuals)
            }
        }
    }

    private companion object {
        const val MAX_VISIBLE_SNACKBARS = 3
    }
}
