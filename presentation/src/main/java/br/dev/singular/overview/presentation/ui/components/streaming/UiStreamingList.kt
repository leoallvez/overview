package br.dev.singular.overview.presentation.ui.components.streaming

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.StreamingUiModel
import br.dev.singular.overview.presentation.ui.components.UiInfoTooltip
import br.dev.singular.overview.presentation.ui.components.UiToolbar
import br.dev.singular.overview.presentation.ui.theme.Background
import br.dev.singular.overview.presentation.ui.utils.getStreamingMocks

@Composable
fun UiStreamingList(
    items: List<StreamingUiModel>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement
            .spacedBy(dimensionResource(R.dimen.spacing_2x))
    ) {
        items(items) {
            UiStreamingItem(it)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
internal fun UiStreamingListPreview() {
    Column(
        modifier = Modifier
            .background(Background)
            .padding(horizontal = dimensionResource(R.dimen.spacing_4x))
    ) {
        UiToolbar("Catálogos")
        UiInfoTooltip(
            "Conteúdo para consulta (sem reprodução).",
            Modifier.padding(bottom = dimensionResource(R.dimen.spacing_3x))
        )
        UiStreamingList(items = getStreamingMocks(20))
    }
}

