package br.dev.singular.overview.presentation.ui.components.snackbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import br.dev.singular.overview.presentation.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

private const val EXIT_ANIMATION_DURATION = 200L

@Composable
fun UiSnackbarHost(
    hostState: UiSnackbarHostState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            verticalArrangement = Arrangement
                .spacedBy(dimensionResource(R.dimen.spacing_2x)),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = dimensionResource(R.dimen.spacing_4x),
                    vertical = dimensionResource(R.dimen.spacing_1x)
                )
        ) {
            hostState.snackbarList.forEach { visuals ->
                StackedSnackbarItem(
                    visuals = visuals,
                    onDismiss = { hostState.dismiss(visuals) }
                )
            }
        }
    }
}

@Composable
private fun StackedSnackbarItem(
    visuals: UiSnackbarVisuals,
    onDismiss: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val startDismissFlow: () -> Unit = {
        coroutineScope.launch {
            visible = false
            delay(duration = EXIT_ANIMATION_DURATION.milliseconds)
            onDismiss()
        }
    }

    LaunchedEffect(Unit) {
        visible = true
        delay(duration = visuals.durationMillis.milliseconds)
        startDismissFlow()
    }

    UiSnackbar(
        visuals = visuals,
        visible = visible,
        onDismiss = startDismissFlow
    )
}
