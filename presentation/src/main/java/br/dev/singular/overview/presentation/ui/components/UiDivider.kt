package br.dev.singular.overview.presentation.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.res.dimensionResource
import br.dev.singular.overview.presentation.R
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
internal fun UiDividerPreview() {
    UiDivider(
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_4x)),
        visible = true
    )
}
