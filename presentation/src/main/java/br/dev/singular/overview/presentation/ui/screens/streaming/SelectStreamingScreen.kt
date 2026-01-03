package br.dev.singular.overview.presentation.ui.screens.streaming

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.model.SelectStreamingUiModel
import br.dev.singular.overview.presentation.model.StreamingUiModel
import br.dev.singular.overview.presentation.tagging.TagManager
import br.dev.singular.overview.presentation.tagging.params.TagCommon
import br.dev.singular.overview.presentation.tagging.params.TagStreaming
import br.dev.singular.overview.presentation.tagging.params.TagStreaming.Detail.STREAMING_CHANGE
import br.dev.singular.overview.presentation.ui.components.UiList
import br.dev.singular.overview.presentation.ui.components.UiScaffold
import br.dev.singular.overview.presentation.ui.components.icon.UiIconButton
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconStyle
import br.dev.singular.overview.presentation.ui.components.streaming.UiStreamingItem
import br.dev.singular.overview.presentation.ui.components.text.UiTitle
import br.dev.singular.overview.presentation.ui.screens.common.UiStateResult
import br.dev.singular.overview.presentation.ui.theme.Background
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import br.dev.singular.overview.presentation.ui.theme.Surface
import br.dev.singular.overview.presentation.ui.utils.getStreamingMocks
import kotlinx.collections.immutable.toImmutableList

/**
 * A screen that allows the user to select a streaming service.
 *
 * @param tagPath The path for analytics tagging.
 * @param uiState The state of the UI, which can be loading, success, or error.
 * @param onLoad A callback to be invoked when the screen needs to load data.
 * @param onBack A callback to be invoked when the user navigates back.
 * @param onSelected A callback to be invoked when the user selects a streaming service.
 */
@Composable
fun SelectStreamingScreen(
    tagPath: String = TagStreaming.PATH,
    uiState: UiState<SelectStreamingUiModel>,
    onLoad: ()-> Unit,
    onBack: () -> Unit,
    onSelected: (StreamingUiModel) -> Unit
) {
    LaunchedEffect(Unit) { onLoad() }
    UiScaffold(
        topBar = {
            Toolbar {
                TagManager.logClick(tagPath, TagCommon.Detail.CLOSE)
                onBack()
            }
        },
    ) { padding ->
        UiStateResult(
            uiState = uiState,
            tagPath = tagPath,
            onRefresh = onLoad
        ) { data ->
            UiList(
                items = data.streaming.toImmutableList(),
                modifier = Modifier.padding(padding),
            ) { 
                UiStreamingItem(
                    model = it,
                    selected = it.id == data.selectedId
                ) {
                    TagManager.logClick(tagPath, STREAMING_CHANGE, it.id)
                    onSelected(it)
                }
            }
        }
    }
}

@Composable
private fun Toolbar(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.spacing_14x))
            .background(Background),
        verticalAlignment = Alignment.CenterVertically
    ) {
        UiTitle(
            text = stringResource(R.string.select_streaming),
            modifier = Modifier.weight(1f),
            color = HighlightColor
        )
        UiIconButton(
            iconStyle = UiIconStyle(
                source = UiIconSource.painter(R.drawable.ic_arrow_up),
                sizeRes = R.dimen.spacing_8x,
            ),
            background = Surface,
            onClick = onBack
        )
    }
}

@Preview
@Composable
private fun SelectStreamingScreenPreview() {
    var selectedId by remember { mutableLongStateOf(value = 0L) }
    SelectStreamingScreen(
        uiState = UiState.Success(
            SelectStreamingUiModel(
                selectedId = selectedId,
                streaming = getStreamingMocks(30).toImmutableList()
            )
        ),
        onLoad = {},
        onBack = {},
        onSelected = {
            selectedId = it.id
        }
    )
}
