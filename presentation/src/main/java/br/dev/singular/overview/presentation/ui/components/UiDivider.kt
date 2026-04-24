package br.dev.singular.overview.presentation.ui.components

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview

@Composable
fun UiDivider(
    visible: Boolean,
    modifier: Modifier = Modifier
) {
    if (visible) {
        HorizontalDivider(color = DarkGray, modifier = modifier)
    }
}

@UiComponentPreview
@Composable
private fun UiDividerPreview() {
    UiDivider(visible = true)
}
