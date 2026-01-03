package br.dev.singular.overview.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.streaming.UiStreamingItem
import br.dev.singular.overview.presentation.ui.utils.getStreamingMocks
import kotlinx.collections.immutable.ImmutableList

/**
 * A generic composable that displays a list of items vertically.
 *
 * @param T The type of the items in the list.
 * @param items The immutable list of items to display.
 * @param modifier The modifier to be applied to the list.
 * @param content The composable content to be displayed for each item.
 */
@Composable
fun <T> UiList(
    items: ImmutableList<T>,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement
            .spacedBy(dimensionResource(R.dimen.spacing_2x))
    ) {
        items(
            items = items,
            key = { item -> item.hashCode() },
        ) { item ->
            content(item)
        }
    }
}

@Preview
@Composable
internal fun UiListPreview() {
    var selectedId by remember { mutableLongStateOf(0) }
    UiList(items = getStreamingMocks(20)) { model ->
        UiStreamingItem(
            model = model,
            selected = model.id == selectedId
        ) {
            selectedId = model.id
        }
    }
}
