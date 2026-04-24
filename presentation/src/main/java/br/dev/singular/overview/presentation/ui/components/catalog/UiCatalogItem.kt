package br.dev.singular.overview.presentation.ui.components.catalog

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.CatalogUiModel
import br.dev.singular.overview.presentation.ui.components.icon.UiIcon
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.style.UiBorderStyle
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import br.dev.singular.overview.presentation.ui.theme.LowlightColor
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview
import br.dev.singular.overview.presentation.ui.utils.border
import br.dev.singular.overview.presentation.ui.utils.fakeCatalog

/**
 * A composable that displays a catalog item.
 *
 * This component can display as a simple item for navigation or as a selectable item
 * within a group, depending on the [selected] parameter.
 *
 * @param model The [CatalogUiModel] to display.
 * @param modifier The modifier to be applied to this component.
 * @param selected If non-null, the item will display a [RadioButton] indicating its selection
 * state. If null, it will display a navigation chevron.
 * @param onClick The callback to be executed when the item is clicked.
 */
@Composable
fun UiCatalogItem(
    modifier: Modifier = Modifier,
    selected: Boolean? = null,
    model: CatalogUiModel,
    onClick: () -> Unit = {}
) {
    UiCatalogCard(
        model = model,
        onClick = onClick,
        selected = selected,
        modifier = modifier.border(
            style = UiBorderStyle(
                visible = selected ?: false,
                color = HighlightColor,
                shape = RoundedCornerShape(
                    size = dimensionResource(R.dimen.corner_width)
                )
            )
        ),
        rightContent = {
            if (selected != null) {
                RadioButton(
                    selected = selected,
                    onClick = onClick,
                    colors = RadioButtonDefaults.colors().copy(
                        selectedColor = HighlightColor,
                        unselectedColor = LowlightColor,
                    )
                )
            } else {
                UiIcon(
                    source = UiIconSource.vector(Icons.Default.ChevronRight),
                    modifier = Modifier.size(dimensionResource(R.dimen.spacing_9x)),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    )
}

@UiComponentPreview
@Composable
private fun UiCatalogItemPreview() {
    UiCatalogItem(model = fakeCatalog().first())
}

@UiComponentPreview
@Composable
private fun UiCatalogItemRadioButtonPreview() {
    var selected by remember { mutableStateOf(false) }
    UiCatalogItem(
        selected = selected,
        model = fakeCatalog().first()
    ) {
        selected = !selected
    }
}
