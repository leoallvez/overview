package br.dev.singular.overview.presentation.ui.components.catalog

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.CatalogUiModel
import br.dev.singular.overview.presentation.ui.components.UiImage
import br.dev.singular.overview.presentation.ui.components.icon.UiIcon
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.style.UiBorderStyle
import br.dev.singular.overview.presentation.ui.components.style.UiImageStyle
import br.dev.singular.overview.presentation.ui.components.text.UiText
import br.dev.singular.overview.presentation.ui.components.text.UiTitle
import br.dev.singular.overview.presentation.ui.theme.LowlightColor
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview
import br.dev.singular.overview.presentation.ui.utils.border
import br.dev.singular.overview.presentation.ui.utils.fakeCatalogs
import com.composables.icons.lucide.CircleAlert
import com.composables.icons.lucide.Lucide
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun UiCatalogList(
    modifier: Modifier = Modifier,
    catalogs: ImmutableList<CatalogUiModel>,
    isReleased: Boolean,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onClick: (CatalogUiModel) -> Unit = {}
) {
    Column(modifier) {
        UiTitle(
            text = stringResource(R.string.where_to_watch),
            modifier = Modifier
                .padding(contentPadding)
                .semantics { heading() }
        )
        if (catalogs.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_1x)),
                contentPadding = contentPadding
            ) {
                items(
                    items = catalogs,
                    key = { it.id },
                    contentType = { "catalog" }
                ) { catalog ->
                    UiCatalogIcon(catalog = catalog, onClick = { onClick(catalog) })
                }
            }
        } else {
            val emptyStateTextRes = if (isReleased) {
                R.string.empty_list_providers
            } else {
                R.string.not_yet_released
            }
            UiCatalogEmptyState(
                modifier = Modifier.padding(contentPadding),
                textRes = emptyStateTextRes
            )
        }
    }
}


@Composable
private fun UiCatalogIcon(
    modifier: Modifier = Modifier,
    catalog: CatalogUiModel,
    onClick: (Long) -> Unit
) {
    val shape = RoundedCornerShape(size = dimensionResource(R.dimen.corner_width))
    UiImage(
        modifier = modifier
            .size(dimensionResource(R.dimen.spacing_12x))
            .clickable(onClick = { onClick(catalog.id) }),
        url = catalog.logoURL,
        contentDescription = catalog.name,
        style = UiImageStyle(
            previewDrawableRes = catalog.previewDrawableRes,
            errorDrawableRes = R.drawable.error_catalog_logo_placeholder,
            shape = shape,
            borderStyle = UiBorderStyle(
                visible = true,
                shape = shape
            )
        )
    )
}

@Composable
private fun UiCatalogEmptyState(
    modifier: Modifier = Modifier,
    @StringRes textRes: Int
) {
    Row(
        modifier = modifier
            .padding(vertical = dimensionResource(R.dimen.spacing_1x))
            .heightIn(min = dimensionResource(R.dimen.spacing_12x))
            .border()
            .padding(horizontal = dimensionResource(R.dimen.spacing_2x)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_2x))
    ) {
        UiText(
            text = stringResource(textRes),
            color = LowlightColor,
        )
        UiIcon(
            source = UiIconSource.vector(icon = Lucide.CircleAlert),
            color = LowlightColor,
        )
    }
}

@UiComponentPreview
@Composable
internal fun UiCatalogListPreview() {
    UiCatalogList(
        catalogs = fakeCatalogs(10).toImmutableList(),
        isReleased = true
    )
}

@UiComponentPreview
@Composable
internal fun UiCatalogListWithContentPaddingPreview() {
    UiCatalogList(
        catalogs = fakeCatalogs(10).toImmutableList(),
        isReleased = true,
        contentPadding = PaddingValues(start = dimensionResource(R.dimen.spacing_4x))
    )
}

@UiComponentPreview
@Composable
internal fun UiCatalogListEmptyReleasedPreview() {
    UiCatalogList(
        catalogs = emptyList<CatalogUiModel>().toImmutableList(),
        isReleased = true,
        contentPadding = PaddingValues(start = dimensionResource(R.dimen.spacing_4x))
    )
}

@UiComponentPreview
@Composable
internal fun UiCatalogListEmptyNotReleasedPreview() {
    UiCatalogList(
        catalogs = emptyList<CatalogUiModel>().toImmutableList(),
        isReleased = false,
        contentPadding = PaddingValues(start = dimensionResource(R.dimen.spacing_4x))
    )
}
