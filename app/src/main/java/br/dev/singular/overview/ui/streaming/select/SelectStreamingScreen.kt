package br.dev.singular.overview.ui.streaming.select

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import br.dev.singular.overview.presentation.R
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
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import br.dev.singular.overview.ui.model.toEntity
import br.dev.singular.overview.ui.model.toUi
import br.dev.singular.overview.ui.theme.PrimaryBackground
import br.dev.singular.overview.ui.theme.SecondaryBackground
import kotlinx.collections.immutable.toImmutableList

@Composable
fun SelectStreamingScreen(
    tagPath: String = TagStreaming.PATH,
    onBack: () -> Unit,
    onToHome: () -> Unit,
    viewModel: SelectStreamingViewModel = hiltViewModel()
) {
    UiScaffold(
        topBar = {
            Toolbar {
                TagManager.logClick(customPath = tagPath, detail = TagCommon.Detail.CLOSE)
                onBack()
            }
        },
        bottomBar = { Spacer(Modifier.size(0.dp)) },
    ) { padding ->
        UiStateResult(
            uiState = viewModel.uiState.collectAsState().value,
            tagPath =  tagPath,
            onRefresh = { viewModel.refresh() }
        ) { data ->
            UiList(
                items = data.list.toImmutableList().toUi(),
                modifier = Modifier.padding(padding),
            ) { model ->
                UiStreamingItem(
                    model = model,
                    selected = model.id == data.selectedId
                ) {
                    TagManager.logClick(
                        customPath = tagPath,
                        detail = STREAMING_CHANGE,
                        id = model.id
                    )
                    viewModel.saveSelectedStreaming(model.toEntity())
                    onToHome()
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
            .background(PrimaryBackground),
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
            background = SecondaryBackground,
            onClick = onBack
        )
    }
}
