package br.dev.singular.overview.presentation.ui.screens.streaming

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.model.SelectStreamingUiModel
import br.dev.singular.overview.presentation.model.StreamingUiModel
import br.dev.singular.overview.presentation.tagging.TagManager
import br.dev.singular.overview.presentation.tagging.params.TagStreaming
import br.dev.singular.overview.presentation.tagging.params.TagStreaming.Detail.STREAMING_CHANGE
import br.dev.singular.overview.presentation.ui.components.UiInfoTooltip
import br.dev.singular.overview.presentation.ui.components.UiList
import br.dev.singular.overview.presentation.ui.components.UiScaffold
import br.dev.singular.overview.presentation.ui.components.UiTopAppBar
import br.dev.singular.overview.presentation.ui.components.streaming.UiStreamingItem
import br.dev.singular.overview.presentation.ui.screens.common.UiStateResult
import br.dev.singular.overview.presentation.ui.utils.getStreamingMocks
import kotlinx.collections.immutable.toImmutableList

@Composable
fun ChooseStreamingScreen(
    tagPath: String = TagStreaming.PATH,
    uiState: UiState<SelectStreamingUiModel>,
    onLoad: ()-> Unit,
    onSelected: (StreamingUiModel) -> Unit
) {
    LaunchedEffect(Unit) { onLoad() }
    UiScaffold(
        topBar = {
            UiTopAppBar(title = "Select Catalog")
        },
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
            var visible by remember { mutableStateOf(true) }
            UiInfoTooltip(
                visible = visible,
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.spacing_3x)),
                message = stringResource(R.string.lorem_ipsum)
            ) {
                visible = false
            }
            UiStateResult(
                uiState = uiState,
                tagPath = tagPath,
                onRefresh = onLoad
            ) { data ->
                UiList(
                    items = data.streaming.toImmutableList()
                ) {
                    UiStreamingItem(model = it) {
                        TagManager.logClick(tagPath, STREAMING_CHANGE, it.id)
                        onSelected(it)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ChooseStreamingScreenPreview() {
    var selectedId by remember { mutableLongStateOf(value = 0L) }
    ChooseStreamingScreen(
        uiState = UiState.Success(
            SelectStreamingUiModel(
                selectedId = selectedId,
                streaming = getStreamingMocks(30).toImmutableList()
            )
        ),
        onLoad = {},
        onSelected = {
            selectedId = it.id
        }
    )
}
