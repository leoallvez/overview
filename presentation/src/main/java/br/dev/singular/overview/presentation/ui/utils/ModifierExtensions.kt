package br.dev.singular.overview.presentation.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import br.dev.singular.overview.presentation.ui.components.style.UiBorderStyle
import br.dev.singular.overview.presentation.ui.theme.Background
import br.dev.singular.overview.presentation.ui.theme.Surface


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
fun Modifier.surfaceBackground() = background(Surface)
