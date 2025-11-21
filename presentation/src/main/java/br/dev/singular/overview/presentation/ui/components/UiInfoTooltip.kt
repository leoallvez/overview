package br.dev.singular.overview.presentation.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.icon.UiIcon
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.style.UiBorderStyle
import br.dev.singular.overview.presentation.ui.components.text.UiText
import br.dev.singular.overview.presentation.ui.theme.Surface
import br.dev.singular.overview.presentation.ui.utils.border

/**
 * A composable that displays an information tooltip with a message and a close button.
 *
 * @param message The message to be displayed in the tooltip.
 * @param modifier The modifier to be applied to the tooltip.
 * @param onClose A callback to be invoked when the close button is clicked.
 */
@Composable
fun UiInfoTooltip(
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    message: String,
    onClose: () -> Unit = {}
) {
    UiAnimatedVisibility(visible) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .border(
                    style = UiBorderStyle(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        shape = RoundedCornerShape(dimensionResource(R.dimen.corner_width))
                    ),
                ),
            shape = RoundedCornerShape(dimensionResource(R.dimen.corner_width)),
            color = Surface,
            tonalElevation = 2.dp
        ) {
            Row(
                modifier = Modifier.padding(dimensionResource(R.dimen.spacing_3x)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                UiIcon(
                    source = UiIconSource.painter(R.drawable.ic_outline_alert),
                    contentDescription = null,
                    color = Color.White
                )

                UiText(
                    text = message,
                    modifier = Modifier.weight(1f)
                        .padding(horizontal = dimensionResource(R.dimen.spacing_3x)),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Start,
                )

                IconButton(
                    onClick = onClose,
                    modifier = Modifier.size(dimensionResource(R.dimen.spacing_5x))
                ) {
                    UiIcon(
                        source = UiIconSource.vector(Icons.Default.Close),
                        contentDescription = stringResource(R.string.close),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Preview
@Composable
internal fun UiInfoTooltipPreview() {
    var visible by remember { mutableStateOf(true) }
    UiInfoTooltip(visible = visible, message = stringResource(R.string.lorem_ipsum)) {
        visible = false
    }
}
