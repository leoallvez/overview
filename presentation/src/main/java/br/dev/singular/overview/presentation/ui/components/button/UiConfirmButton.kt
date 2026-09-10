package br.dev.singular.overview.presentation.ui.components.button

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Settings2

@Composable
internal fun UiConfirmButton(
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    UiActionButton(
        text = stringResource(R.string.confirm),
        icon = UiIconSource.vector(Lucide.Settings2),
        enabled = enabled,
        modifier = modifier,
        onClick = onClick,
    )
}

@UiComponentPreview
@Composable
internal fun UiConfirmButtonEnabledPreview() {
    UiConfirmButton(
        enabled = true,
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_2x)),
        onClick = {}
    )
}

@UiComponentPreview
@Composable
internal fun UiConfirmButtonDisabledPreview() {
    UiConfirmButton(
        enabled = false,
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_2x)),
        onClick = {}
    )
}
