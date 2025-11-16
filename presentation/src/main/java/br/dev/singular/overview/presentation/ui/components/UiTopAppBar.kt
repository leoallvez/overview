package br.dev.singular.overview.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.MediaUiType
import br.dev.singular.overview.presentation.ui.components.icon.UiIconButton
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconStyle
import br.dev.singular.overview.presentation.ui.components.media.UiMediaGrid
import br.dev.singular.overview.presentation.ui.components.media.UiMediaTypeSelector
import br.dev.singular.overview.presentation.ui.components.text.UiTitle
import br.dev.singular.overview.presentation.ui.theme.Background
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import br.dev.singular.overview.presentation.ui.theme.Surface
import br.dev.singular.overview.presentation.ui.utils.getMediaMocks


@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    name = "Vertical",
    widthDp = 430,
    heightDp = 960,
    backgroundColor = 0xFF000000,
    showBackground = true
)
@Composable
fun SimpleCollapsingAppBarScreen() {

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState()
    )

    val scrollDirection = remember { mutableStateOf("up") }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                if (consumed.y < 0) {
                    scrollDirection.value = "down"
                } else if (consumed.y > 0) {
                    scrollDirection.value = "up"
                }
                return super.onPostScroll(consumed, available, source)
            }
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(nestedScrollConnection).padding(horizontal = dimensionResource(R.dimen.spacing_4x)),
        topBar = {
            AnimatedVisibility(scrollDirection.value == "down") {
                Row(Modifier.padding(vertical = 8.dp)) {
                    UiIconButton(
                        iconStyle = UiIconStyle(
                            source = UiIconSource.vector(Icons.AutoMirrored.Filled.KeyboardArrowLeft)
                        ),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {}
                    UiTitle("Title")
                }
            }
            AnimatedVisibility(scrollDirection.value == "up") {
                Column(Modifier.padding(vertical = 8.dp)) {
                    UiIconButton(
                        iconStyle = UiIconStyle(
                            source = UiIconSource.vector(Icons.AutoMirrored.Filled.KeyboardArrowLeft)
                        ),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {}
                    Row(
                        Modifier.padding(top = 16.dp, bottom = 8.dp),
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                        UiImage(
                            url = "",
                            previewPainter = painterResource(R.drawable.scifi_stream),
                            errorDefaultImage = R.drawable.launcher_playstore,
                            placeholder = R.drawable.launcher_playstore,
                            modifier = Modifier.size(dimensionResource(R.dimen.spacing_18x))
                        )
                        UiTitle("Title", modifier = Modifier.padding(start = 12.dp))
                    }
                }
            }
        },
        containerColor = Background,
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            AnimatedVisibility(scrollDirection.value == "up") {
                var type by remember { mutableStateOf(MediaUiType.ALL) }
                UiMediaTypeSelector(
                    type,
                    modifier = Modifier.padding(bottom = dimensionResource(R.dimen.spacing_4x)),
                ) { newType ->
                    type = newType
                }
            }



            UiMediaGrid(
                items = getMediaMocks(90),
            )

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UiAppBar(scrollBehavior: TopAppBarScrollBehavior) {

    val currentOffset = scrollBehavior.state.heightOffset

    val isExpanded = remember {
        derivedStateOf {
            currentOffset >= -0.00f
        }
    }

    LargeTopAppBar(
        title = {
            UiTitle(
                text = "Title",
                color = HighlightColor,
                modifier = if (isExpanded.value) Modifier.offset(x = (-16).dp) else Modifier
            )
        },
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            UiIconButton(
                iconStyle = UiIconStyle(
                    source = UiIconSource.vector(Icons.AutoMirrored.Filled.KeyboardArrowLeft),
                    descriptionRes = R.string.backstack_icon,
                ),
                modifier = Modifier.offset(x = (-4).dp).padding(end = 20.dp)
            ) {}
        },
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = Background,
            scrolledContainerColor = Background,
        ),
        expandedHeight = 120.dp,
        collapsedHeight = dimensionResource(R.dimen.spacing_14x)
    )
}

