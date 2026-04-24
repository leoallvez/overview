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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.icon.UiIcon
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.text.UiText
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import br.dev.singular.overview.presentation.ui.theme.LowlightColor
import br.dev.singular.overview.presentation.ui.theme.Surface
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview

/**
 * A custom styled chip that can be activated and deactivated.
 *
 * @param text The text to display on the chip.
 * @param modifier The modifier to be applied to the chip.
 * @param activated Whether the chip is currently activated.
 * @param icon The icon to display on the chip.
 * @param onClick The callback to be invoked when the chip is clicked.
 */
@Composable
fun UiChip(
    text: String,
    modifier: Modifier = Modifier,
    activated: Boolean = false,
    highlightColor: Color = HighlightColor,
    lowlightColor: Color = LowlightColor,
    shape: RoundedCornerShape = RoundedCornerShape(percent = 20),
    icon: @Composable (() -> Unit) = {},
    onClick: () -> Unit
) {
    val color = if (activated) highlightColor else lowlightColor
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
        shape = shape,
        trailingIcon = icon,
        border = BorderStroke(dimensionResource(R.dimen.border_width), color),
    )
}

@UiComponentPreview
@Composable
private fun UiChipActivatedPreview() {
    UiChipPreviewHelper(initialState = true)
}

@UiComponentPreview
@Composable
private fun UiChipNotActivatedPreview() {
    UiChipPreviewHelper(initialState = false)
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
                    source = UiIconSource.vector(Icons.Filled.Clear),
                    color = HighlightColor
                )
            }
        }
    ) {
        activated = !activated
    }
}