package br.dev.singular.overview.presentation.ui.components.button

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.button.style.UiActionButtonStyle
import br.dev.singular.overview.presentation.ui.components.button.style.UiButtonStyle
import br.dev.singular.overview.presentation.ui.components.icon.UiIcon
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconStyle
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Settings2

/**
 * A standard action button component that supports an optional icon and full-width layout.
 *
 * This component wraps [UiButtonBase] and applies a [RoundedCornerShape] based on the style's
 * corner radius. If an icon source is provided in the [UiButtonStyle.icon], it will be displayed
 * before the text with a spacer.
 *
 * @param text The text to be displayed on the button.
 * @param modifier The [Modifier] to be applied to the button. It defaults to [Modifier.fillMaxWidth]
 *                 inside [UiButtonBase].
 * @param style The [UiButtonStyle] defining the appearance and icon configuration.
 *              Defaults to [UiActionButtonStyle].
 * @param enabled Whether the button is interactive and enabled.
 * @param onClick Callback to be invoked when the button is clicked.
 */
@Composable
internal fun UiActionButton(
    text: String,
    modifier: Modifier = Modifier,
    icon: UiIconStyle? = null,
    style: UiButtonStyle = UiActionButtonStyle(),
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    UiButtonBase(
        text = text,
        modifier = modifier.fillMaxWidth(),
        style = style,
        enabled = enabled,
        shape = RoundedCornerShape(dimensionResource(style.cornerRadius)),
        onClick = onClick
    ) {
        icon?.source?.let { iconSource ->
            UiIcon(
                source = iconSource,
                color = style.colors.borderColor(enabled),
                modifier = Modifier
                    .size(dimensionResource(R.dimen.spacing_6x))
            )
            Spacer(modifier = Modifier
                .width(dimensionResource(R.dimen.spacing_2x)))
        }
    }
}

@UiComponentPreview
@Composable
internal fun UiActionButtonEnabledWithIconPreview() {
    UiActionButton(
        text = "Filter",
        icon = UiIconStyle(
            source = UiIconSource.vector(Lucide.Settings2)
        ),
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_2x)),
        onClick = {}
    )
}

@UiComponentPreview
@Composable
internal fun UiActionButtonEnabledWithoutIconPreview() {
    UiActionButton(
        text = "Filter",
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_2x)),
        onClick = {}
    )
}

@UiComponentPreview
@Composable
internal fun UiActionButtonDisabledWithIconPreview() {
    UiActionButton(
        text = "Filter",
        icon = UiIconStyle(
            source = UiIconSource.vector(Lucide.Settings2)
        ),
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_2x)),
        enabled = false,
        onClick = {}
    )
}

@UiComponentPreview
@Composable
internal fun UiActionButtonDisabledWithoutIconPreview() {
    UiActionButton(
        text = "Filter",
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_2x)),
        enabled = false,
        onClick = {}
    )
}
