package br.dev.singular.overview.presentation.ui.components.streaming

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.StreamingUiModel
import br.dev.singular.overview.presentation.ui.utils.getStreamingMocks

@Composable
fun UiStreamingList(
    items: List<StreamingUiModel>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_2x))
    ) {
        items(items) {
            UiStreamingItem(it)
        }
    }
}

@Preview(backgroundColor = 0x000000)
@Composable
internal fun UiStreamingListPreview() {
    UiStreamingList(items = getStreamingMocks(20))
}
