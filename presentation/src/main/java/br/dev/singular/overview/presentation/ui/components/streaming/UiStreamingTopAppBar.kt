package br.dev.singular.overview.presentation.ui.components.streaming

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.MediaUiType
import br.dev.singular.overview.presentation.model.StreamingUiModel
import br.dev.singular.overview.presentation.ui.components.UiChip
import br.dev.singular.overview.presentation.ui.components.UiImage
import br.dev.singular.overview.presentation.ui.components.media.UiMediaGrid
import br.dev.singular.overview.presentation.ui.components.media.UiMediaTypeSelector
import br.dev.singular.overview.presentation.ui.components.text.UiTitle
import br.dev.singular.overview.presentation.ui.theme.Background
import br.dev.singular.overview.presentation.ui.theme.DefaultTextColor
import br.dev.singular.overview.presentation.ui.utils.getMediaMocks
import br.dev.singular.overview.presentation.ui.utils.getStreamingMocks
import br.dev.singular.overview.presentation.ui.utils.rememberCollapseScrollConnection

@Composable
fun UiStreamingTopAppBar(
    streaming: StreamingUiModel,
    isCollapsed: Boolean,
    onCatalogClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .sizeIn(minHeight = dimensionResource(R.dimen.spacing_14x))
            .padding(vertical = dimensionResource(R.dimen.spacing_3x))
            .animateContentSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        UiImage(
            url = streaming.logoURL,
            previewPainter = streaming.previewContent,
            errorDefaultImage = R.drawable.launcher_playstore,
            placeholder = R.drawable.launcher_playstore,
            modifier = Modifier
                .size(
                    dimensionResource(
                        if (isCollapsed) R.dimen.spacing_12x else R.dimen.spacing_18x
                    )
                )
        )

        UiTitle(
            text = streaming.name,
            modifier = Modifier
                .padding(horizontal = dimensionResource(R.dimen.spacing_3x))
                .weight(1f),
            color = DefaultTextColor
        )

        UiChip(
            text = stringResource(R.string.catalogs),
            activated = true,
            shape = RoundedCornerShape(20),
            highlightColor = DefaultTextColor,
            onClick = onCatalogClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    name = "Vertical",
    widthDp = 430,
    heightDp = 960,
    backgroundColor = 0xFF000000,
    showBackground = true
)
@Composable
fun UiTopAppBarPreview() {

    var isCollapsed by rememberSaveable { mutableStateOf(false) }

    val nestedScrollConnection = rememberCollapseScrollConnection {
        isCollapsed = it
    }

    Scaffold(
        modifier = Modifier
            .nestedScroll(nestedScrollConnection)
            .padding(horizontal = dimensionResource(R.dimen.spacing_4x)),
        topBar = {
            UiStreamingTopAppBar(
                streaming = getStreamingMocks().first().copy(name = "Netflix"),
                isCollapsed
            )
        },
        containerColor = Background,
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            var type by rememberSaveable { mutableStateOf(MediaUiType.ALL) }
            AnimatedVisibility(!isCollapsed) {
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
