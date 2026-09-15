package br.dev.singular.overview.presentation.ui.components.snackbar

import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A snackbar being displayed by [UiSnackbarHost].
 *
 * The [id] is unique per call to [UiSnackbarHostState.show], so equal [visuals] shown more than
 * once remain distinct entries.
 */
internal class UiSnackbarEntry(
    val id: Long,
    val visuals: UiSnackbarVisuals
)

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
     * A reactive list of currently active snackbars displayed by [UiSnackbarHost].
     */
    internal val snackbarList: List<UiSnackbarEntry>
        field = mutableStateListOf<UiSnackbarEntry>()

    private var nextId = 0L

    private fun addSnackbar(visuals: UiSnackbarVisuals) {
        if (snackbarList.size >= MAX_VISIBLE_SNACKBARS) {
            snackbarList.removeAt(0)
        }
        snackbarList.add(UiSnackbarEntry(id = nextId++, visuals = visuals))
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
     * Dismisses and removes a specific [entry] from the active list.
     *
     * Ensures thread-safe execution on the main thread.
     *
     * @param entry The [UiSnackbarEntry] to be dismissed.
     */
    internal fun dismiss(entry: UiSnackbarEntry) {
        if (android.os.Looper.myLooper() == android.os.Looper.getMainLooper()) {
            removeSnackbar(entry)
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                removeSnackbar(entry)
            }
        }
    }

    private fun removeSnackbar(entry: UiSnackbarEntry) {
        snackbarList.removeAll { it.id == entry.id }
    }

    private companion object {
        const val MAX_VISIBLE_SNACKBARS = 3
    }
}
