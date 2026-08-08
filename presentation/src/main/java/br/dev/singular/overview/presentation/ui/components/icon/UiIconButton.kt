package br.dev.singular.overview.presentation.ui.components.icon

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconStyle
import br.dev.singular.overview.presentation.ui.components.style.UiBorderStyle
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview
import br.dev.singular.overview.presentation.ui.utils.border

/**
 * A circular icon button composable.
 *
 * @param iconStyle The style of the icon to be displayed.
 * @param modifier The modifier to be applied to this icon button.
 * @param borderStyle The style of the border.
 * @param background The background color of the button.
 * @param onClick The lambda to be executed when the button is clicked.
 */
@Composable
fun UiIconButton(
    iconStyle: UiIconStyle,
    modifier: Modifier = Modifier,
    borderStyle: UiBorderStyle = UiBorderStyle(),
    background: Color = Color.White.copy(alpha = 0.1f),
    onClick: () -> Unit = {}
) {
    Box(
        modifier
            .clip(CircleShape)
            .background(background)
            .size(dimensionResource(id = R.dimen.spacing_8x))
            .clickable(onClick = onClick)
            .border(borderStyle.copy(shape = CircleShape))
    ) {
        Box(Modifier.align(Alignment.Center)) {
            iconStyle.apply {
                UiIcon(
                    source = source,
                    contentDescription = descriptionRes?.let { stringResource(it) } ?: "",
                    modifier = iconStyle.modifier.size(dimensionResource(sizeRes)),
                    color = color
                )
            }
        }
    }
}

@UiComponentPreview
@Composable
internal fun UiIconButtonVectorPreview() {
    UiIconButton(
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_4x)),
        iconStyle = UiIconStyle(
            source = UiIconSource.vector(Icons.AutoMirrored.Filled.KeyboardArrowLeft)
        )
    )
}

@UiComponentPreview
@Composable
internal fun UiIconButtonPainterPreview() {
    UiIconButton(
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_4x)),
        iconStyle = UiIconStyle(
            source = UiIconSource.painter(R.drawable.ic_arrow_up)
        )
    )
}
