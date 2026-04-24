package br.dev.singular.overview.presentation.ui.components.catalog

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.CatalogUiModel
import br.dev.singular.overview.presentation.ui.components.UiImage
import br.dev.singular.overview.presentation.ui.components.UiScaffold
import br.dev.singular.overview.presentation.ui.components.media.UiMediaGrid
import br.dev.singular.overview.presentation.ui.components.shimmer.UiShimmerBox
import br.dev.singular.overview.presentation.ui.components.style.UiBorderStyle
import br.dev.singular.overview.presentation.ui.components.style.UiImageStyle
import br.dev.singular.overview.presentation.ui.components.text.UiTitle
import br.dev.singular.overview.presentation.ui.utils.UiScreenPreview
import br.dev.singular.overview.presentation.ui.utils.fakeCatalog
import br.dev.singular.overview.presentation.ui.utils.fakeMedias
import br.dev.singular.overview.presentation.ui.utils.rememberCollapseScrollConnection

/**
 * A top app bar component tailored for displaying information about a catalog.
 * It features a logo and the catalog's name, with support for a collapsed state.
 *
 * @param catalog The [CatalogUiModel] containing the catalog's data.
 * @param modifier The [Modifier] to be applied to this component.
 * @param isCollapsed Whether the top app bar is in its collapsed state.
 */
@Composable
fun UiCatalogTopAppBar(
    catalog: CatalogUiModel?,
    modifier: Modifier = Modifier,
    isCollapsed: Boolean,
) {
    val logoSize by animateDpAsState(
        targetValue = dimensionResource(
            if (isCollapsed) R.dimen.spacing_12x else R.dimen.spacing_18x
        ),
        label = "logoSize"
    )

    val verticalPadding by animateDpAsState(
        targetValue = dimensionResource(
            if (isCollapsed) R.dimen.spacing_2x else R.dimen.spacing_3x
        ),
        label = "verticalPadding"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = verticalPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement
            .spacedBy(dimensionResource(R.dimen.spacing_3x))
    ) {
        CatalogLogo(
            catalog = catalog,
            size = logoSize
        )

        CatalogTitle(
            name = catalog?.name,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun CatalogLogo(
    catalog: CatalogUiModel?,
    size: Dp
) {
    if (catalog == null) {
        UiShimmerBox(
            modifier = Modifier.size(size),
            shape = RoundedCornerShape(
                size = dimensionResource(R.dimen.spacing_2x)
            )
        )
    } else {
        UiImage(
            url = catalog.logoURL,
            contentDescription = catalog.name,
            modifier = Modifier.size(size),
            style = UiImageStyle(
                errorDrawableRes = R.drawable.launcher_playstore,
                previewDrawableRes = catalog.previewDrawableRes,
                borderStyle = UiBorderStyle(visible = true)
            )
        )
    }
}

@Composable
private fun CatalogTitle(
    name: String?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        if (name == null) {
            UiShimmerBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.spacing_5x)),
                shape = RoundedCornerShape(dimensionResource(R.dimen.spacing_1x))
            )
        } else {
            UiTitle(text = name)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@UiScreenPreview
@Composable
private fun UiCatalogTopAppBarPreview() {
    var isCollapsed by rememberSaveable { mutableStateOf(false) }
    val nestedScrollConnection = rememberCollapseScrollConnection { isCollapsed = it }

    UiScaffold(
        modifier = Modifier.nestedScroll(nestedScrollConnection),
        topBar = {
            UiCatalogTopAppBar(
                catalog = fakeCatalog().first(),
                isCollapsed = isCollapsed
            )
        },
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            UiMediaGrid(items = fakeMedias(90))
        }
    }
}
