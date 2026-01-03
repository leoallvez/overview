package br.dev.singular.overview.presentation.ui.screens.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle.Event.*
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

/**
 * A composable that observes the lifecycle of its owner and calls the provided callbacks
 * for pause and resume events.
 *
 * @param onPause A callback to be invoked when the `ON_PAUSE` lifecycle event occurs.
 * @param onResume A callback to be invoked when the `ON_RESUME` lifecycle event occurs.
 */
@Composable
fun UiLifecycle(onPause: () -> Unit, onResume: () -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == ON_PAUSE) {
                onPause.invoke()
            }
            if (event == ON_RESUME) {
                onResume.invoke()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
}
