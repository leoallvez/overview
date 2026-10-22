package br.dev.singular.overview.presentation.ui.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import br.dev.singular.overview.presentation.ui.components.snackbar.UiSnackbarHost
import br.dev.singular.overview.presentation.ui.components.snackbar.UiSnackbarHostState

@Composable
fun UiSnackbarPreview(
    modifier: Modifier = Modifier,
    hostState: UiSnackbarHostState = remember { UiSnackbarHostState() },
    content: @Composable (hostState: UiSnackbarHostState) -> Unit
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = {
            UiSnackbarHost(hostState)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            content(hostState)
        }
    }
}
