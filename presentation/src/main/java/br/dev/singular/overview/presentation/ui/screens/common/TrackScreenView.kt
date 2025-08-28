package br.dev.singular.overview.presentation.ui.screens.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import br.dev.singular.overview.presentation.tagging.TagManager

@Composable
fun TrackScreenView(
    tagPath: String,
    status: String = ""
) {
    DisposableEffect(Unit) {
        TagManager.logScreenView(tagPath, status)
        onDispose { /* no-op */ }
    }
}
