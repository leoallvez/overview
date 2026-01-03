package br.dev.singular.overview.presentation.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource

@Composable
fun rememberCollapseScrollConnection(
    onCollapsedStateChange: (Boolean) -> Unit
) = remember {
    object : NestedScrollConnection {
        override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource
        ): Offset {

            val isCollapsed = consumed.y < 0
            onCollapsedStateChange(isCollapsed)

            return super.onPostScroll(consumed, available, source)
        }
    }
}
