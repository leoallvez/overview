package br.dev.singular.overview.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.catalog.UiCatalogItem
import br.dev.singular.overview.presentation.ui.components.text.UiTitle
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview
import br.dev.singular.overview.presentation.ui.utils.fakeCatalogs
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
    firstItem: @Composable () -> Unit = {},
    content: @Composable (T) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement
            .spacedBy(dimensionResource(R.dimen.spacing_2x))
    ) {
        item { firstItem() }
        items(
            items = items
        ) { item ->
            content(item)
        }
    }
}

@UiComponentPreview
@Composable
internal fun UiListPreview() {
    var selectedId by remember { mutableLongStateOf(0) }
    UiList(
        items = fakeCatalogs(20),
        firstItem = {
            UiTitle(
                text = "First Item",
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )
        }
    ) { model ->
        UiCatalogItem(
            model = model,
            selected = model.id == selectedId
        ) {
            selectedId = model.id
        }
    }
}
