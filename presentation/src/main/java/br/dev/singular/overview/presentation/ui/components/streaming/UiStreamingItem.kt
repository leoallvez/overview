package br.dev.singular.overview.presentation.ui.components.streaming

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
import androidx.compose.ui.tooling.preview.Preview
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.StreamingUiModel
import br.dev.singular.overview.presentation.ui.components.icon.UiIcon
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.style.UiBorderStyle
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import br.dev.singular.overview.presentation.ui.theme.LowlightColor
import br.dev.singular.overview.presentation.ui.utils.border
import br.dev.singular.overview.presentation.ui.utils.getStreamingMocks

/**
 * A composable that displays a streaming service item, intended for navigation.
 *
 * @param model The [StreamingUiModel] to display.
 * @param modifier The modifier to be applied to this component.
 * @param onClick The callback to be executed when the item is clicked.
 */
@Composable
fun UiStreamingItem(
    model: StreamingUiModel,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    UiStreamingCard(
        model = model,
        modifier = modifier,
        onClick = onClick,
        rightContent = {
            UiIcon(
                source = UiIconSource.vector(Icons.Default.ChevronRight),
                modifier = Modifier.size(dimensionResource(R.dimen.spacing_9x)),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    )
}

/**
 * A composable that displays a selectable streaming service item.
 *
 * @param selected Whether the item is currently selected.
 * @param model The [StreamingUiModel] to display.
 * @param modifier The modifier to be applied to this component.
 * @param onClick The callback to be executed when the item is clicked.
 */
@Composable
fun UiStreamingItem(
    selected: Boolean,
    model: StreamingUiModel,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    UiStreamingCard(
        model = model,
        modifier = modifier.border(
            style = UiBorderStyle(
                visible = selected,
                color = HighlightColor,
                shape = RoundedCornerShape(
                    size = dimensionResource(R.dimen.corner_width)
                )
            )
        ),
        onClick = onClick,
        rightContent = {
            RadioButton(
                selected = selected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors().copy(
                    selectedColor = HighlightColor,
                    unselectedColor = LowlightColor,
                )
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
internal fun UiStreamingItemPreview() {
    UiStreamingItem(model = getStreamingMocks().first())
}

@Preview(showBackground = true)
@Composable
internal fun UiStreamingItemRadioButtonPreview() {
    var selected by remember { mutableStateOf(false) }
    UiStreamingItem(
        selected = selected,
        model = getStreamingMocks().first()
    ) {
        selected = !selected
    }
}
