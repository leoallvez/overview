package br.dev.singular.overview.presentation.ui.components.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.icon.UiIconButton
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconStyle
import br.dev.singular.overview.presentation.ui.components.navigation.UiTopAppBarBackPosition.LEADING
import br.dev.singular.overview.presentation.ui.components.navigation.UiTopAppBarBackPosition.TRAILING
import br.dev.singular.overview.presentation.ui.components.text.UiTitle
import br.dev.singular.overview.presentation.ui.components.user.UiUserAvatar
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview
import com.composables.icons.lucide.ChevronDown
import com.composables.icons.lucide.ChevronLeft
import com.composables.icons.lucide.Lucide

/**
 * A custom toolbar composable that displays a title.
 *
 * @param title The title to be displayed in the center of the toolbar.
 * @param modifier The [Modifier] to be applied to the toolbar.
 */
@Composable
internal fun UiTopAppBar(
    title: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.spacing_14x))
    ) {
        UiTitle(
            text = title,
            modifier = Modifier.align(Alignment.CenterStart),
            color = HighlightColor
        )
    }
}

/**
 * A custom toolbar composable that displays a title and a trailing content.
 *
 * @param title The title to be displayed at the start of the toolbar.
 * @param trailingContent The content to be displayed at the end of the toolbar.
 * @param modifier The [Modifier] to be applied to the toolbar.
 */
@Composable
internal fun UiTopAppBar(
    title: String,
    trailingContent: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.spacing_14x)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        UiTitle(
            text = title,
            modifier = Modifier.weight(1f),
            color = HighlightColor
        )
        trailingContent()
    }
}

@Composable
internal fun UiTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    backPosition: UiTopAppBarBackPosition = TRAILING,
    onBack: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.spacing_14x)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (backPosition == LEADING) {
            UiTitleBarAction(
                icon = Lucide.ChevronLeft,
                modifier = Modifier.padding(
                    end = dimensionResource(R.dimen.spacing_2x)
                ),
                onClick = onBack
            )
        }

        UiTitle(
            text = title,
            modifier = Modifier.weight(1f),
            color = HighlightColor
        )

        if (backPosition == TRAILING) {
            UiTitleBarAction(icon = Lucide.ChevronDown, onClick = onBack)
        }
    }
}

@Composable
private fun UiTitleBarAction(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    UiIconButton(
        modifier = modifier,
        iconStyle = UiIconStyle(
            source = UiIconSource.vector(icon),
            sizeRes = R.dimen.spacing_8x,
        ),
        onClick = onClick
    )
}

@UiComponentPreview
@Composable
internal fun UiToolbarDefaultPreview() {
    UiTopAppBar(
        title = "Title",
        modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.spacing_4x))
    )
}

@UiComponentPreview
@Composable
internal fun UiToolbarTrailingPreview() {
    UiTopAppBar(
        title = "Title",
        modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.spacing_4x))
    ) { }
}

@UiComponentPreview
@Composable
internal fun UiToolbarLeadingPreview() {
    UiTopAppBar(
        title = "Title",
        backPosition = LEADING,
        modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.spacing_4x))
    ) { }
}

@UiComponentPreview
@Composable
internal fun UiToolbarTrailingContentPreview() {
    UiTopAppBar(
        title = "Title",
        trailingContent = {
            UiUserAvatar(
                url = "https://imagens.com/user.jpg",
                previewDrawableRes = R.drawable.sample_profile,
                onClick = {}
            )
        },
        modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.spacing_4x))
    )
}
