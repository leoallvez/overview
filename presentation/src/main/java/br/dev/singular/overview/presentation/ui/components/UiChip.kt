package br.dev.singular.overview.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.text.UiText
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import br.dev.singular.overview.presentation.ui.theme.LowlightColor
import br.dev.singular.overview.presentation.ui.theme.Surface

@Composable
fun UiChip(
    text: String,
    modifier: Modifier = Modifier,
    activated: Boolean = false,
    icon: @Composable (() -> Unit) = {},
    onClick: () -> Unit
) {
    val color = if (activated) HighlightColor else LowlightColor
    FilterChip(
        onClick = onClick,
        modifier = modifier.height(dimensionResource(R.dimen.spacing_7x)),
        label = {
            UiText(text, isBold = activated, color = color)
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Surface,
        ),
        selected = activated,
        shape = RoundedCornerShape(percent = 50),
        trailingIcon = icon,
        border = BorderStroke(dimensionResource(R.dimen.border_width), color),
    )
}

@Preview(name = "Activated")
@Composable
internal fun UiChipActivatedPreview() {
    UiChipPreviewHelper(true)
}

@Preview(name = "Not activated")
@Composable
internal fun UiChipNotActivatedPreview() {
    UiChipPreviewHelper(false)
}

@Composable
private fun UiChipPreviewHelper(initialState: Boolean) {
    var activated by rememberSaveable { mutableStateOf(initialState) }
    UiChip(
        text = "Label",
        activated = activated,
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_1x)),
        icon = {
            if (activated) {
                UiIcon(
                    icon = Icons.Filled.Clear,
                    color = HighlightColor
                )
            }
        }
    ) {
        activated = !activated
    }
}
