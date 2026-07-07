package br.dev.singular.overview.presentation.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.theme.BorderColor
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview

@Composable
fun UiDivider(
    modifier: Modifier = Modifier,
    visible: Boolean = true
) {
    if (visible) {
        HorizontalDivider(color = BorderColor, modifier = modifier)
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
