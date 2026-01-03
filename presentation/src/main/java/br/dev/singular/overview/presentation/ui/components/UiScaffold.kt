package br.dev.singular.overview.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.media.UiMediaGrid
import br.dev.singular.overview.presentation.ui.theme.Background
import br.dev.singular.overview.presentation.ui.utils.getMediaMocks

/**
 * A custom Scaffold composable that provides a consistent layout structure for screens in the app.
 *
 * @param modifier The modifier to be applied to the Scaffold.
 * @param topBar The composable to be displayed at the top of the screen.
 * @param padding The padding to be applied to the content of the Scaffold.
 * @param bottomBar The composable to be displayed at the bottom of the screen.
 * @param content The main content of the screen.
 */
@Composable
fun UiScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    padding: PaddingValues = PaddingValues(horizontal = dimensionResource(R.dimen.spacing_4x)),
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        containerColor = Background,
        modifier = modifier
            .background(Background)
            .padding(padding),
        topBar = topBar,
        bottomBar = bottomBar,
    ) { padding ->
        content.invoke(padding)
    }
}

@Preview(
    name = "Vertical",
    widthDp = 300,
    heightDp = 600
)
@Composable
internal fun UiScaffoldPreview() {
    UiScaffold(
        topBar = { UiTopAppBar("Screen Title") }
    ) {
        Box (Modifier.padding(top = it.calculateTopPadding())) {
            UiMediaGrid(
                items = getMediaMocks(10),
                modifier = Modifier.fillMaxSize().background(Background)
            )
        }
    }
}
