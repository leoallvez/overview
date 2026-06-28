package br.dev.singular.overview.presentation.ui.components.navigation.bottom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.icon.UiIcon
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.text.UiText
import br.dev.singular.overview.presentation.ui.theme.Background
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import br.dev.singular.overview.presentation.ui.theme.LowlightColor
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview
import br.dev.singular.overview.presentation.ui.utils.UiSlotHelper

@Composable
fun UiBottomNavigation(
    modifier: Modifier = Modifier,
    state: IUiBottomNavigationState,
    topSlot: @Composable () -> Unit = {},
) {
    if (!state.visible) return
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_1x))
    ) {
        topSlot()
        NavigationBar(
            modifier = Modifier.height(dimensionResource(R.dimen.spacing_16x)),
            containerColor = Background,
            windowInsets = WindowInsets(0)
        ) {
            state.navItems.forEach { item ->
                val selected = state.currentRoute == item.destination.route
                val color = if (selected) HighlightColor else LowlightColor
                NavigationBarItem(
                    icon = {
                        UiIcon(
                            source = UiIconSource.vector(item.icon),
                            color = color,
                            modifier = Modifier.size(dimensionResource(R.dimen.spacing_6x))
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Background
                    ),
                    label = { UiText(stringResource(item.title), color = color) },
                    selected = selected,
                    onClick = { state.onSelectItem(item) }
                )
            }
        }
    }
}

@UiComponentPreview
@Composable
internal fun UiBottomNavigationPreview() {
    UiBottomNavigation(
        state = rememberUiBottomNavigationState(),
        topSlot = { UiSlotHelper("Top Slot Area") }
    )
}
