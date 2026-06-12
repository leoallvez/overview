package br.dev.singular.overview.presentation.ui.screens.catalog.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.MediaUiType
import br.dev.singular.overview.presentation.model.QueryUiState
import br.dev.singular.overview.presentation.ui.components.UiAnimatedVisibility
import br.dev.singular.overview.presentation.ui.components.UiChip
import br.dev.singular.overview.presentation.ui.components.icon.UiIcon
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.theme.DefaultTextColor
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import br.dev.singular.overview.presentation.ui.theme.LowlightColor
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview
import br.dev.singular.overview.presentation.ui.utils.fakeGenres
import br.dev.singular.overview.presentation.ui.utils.localizedName

sealed interface UiFilterType {
    data class Type(val value: MediaUiType) : UiFilterType
    data object Genre : UiFilterType
    data object Catalog : UiFilterType
}

@Composable
fun UiMainFilter(
    query: QueryUiState,
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    onClickFilter: (UiFilterType) -> Unit = {}
) {
    val isAll = query.type == MediaUiType.ALL

    UiAnimatedVisibility(visible = visible) {
        LazyRow(
            modifier = modifier,
            horizontalArrangement = Arrangement
                .spacedBy(dimensionResource(R.dimen.spacing_2x)),
        ) {
            if (isAll) {
                items(
                    items = MediaUiType.entries,
                    key = { it.name }
                ) { type ->
                    val activated = type == query.type
                    TypeChip(
                        type = type,
                        activated = activated,
                        onClick = {
                            onClickFilter(
                                UiFilterType.Type(if (activated) MediaUiType.ALL else type)
                            )
                        }
                    )
                }
            } else {
                item(key = query.type.name) {
                    TypeChip(
                        type = query.type,
                        activated = true,
                        onClick = { onClickFilter(UiFilterType.Type(MediaUiType.ALL)) }
                    )
                }

                item(key = "genre") {
                    GenreChip(
                        genre = query.genre?.localizedName(),
                        onClick = { onClickFilter(UiFilterType.Genre) }
                    )
                }
            }

            item(key = "catalog") {
                CatalogChip(onClick = { onClickFilter(UiFilterType.Catalog) })
            }
        }
    }
}

@Composable
private fun TypeChip(
    type: MediaUiType,
    activated: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    UiChip(
        modifier = modifier,
        text = stringResource(type.labelRes),
        onClick = onClick,
        activated = activated,
        icon = {
            if (type != MediaUiType.ALL && activated) {
                UiIcon(source = UiIconSource.vector(Icons.Filled.Clear))
            }
        }
    )
}

@Composable
private fun GenreChip(
    genre: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activated = genre.isNullOrEmpty().not()
    UiChip(
        modifier = modifier,
        text = genre ?: stringResource(R.string.genre),
        onClick = onClick,
        activated = activated,
        icon = {
            UiIcon(
                source = UiIconSource.painter(R.drawable.ic_arrow_down),
                color = if (activated) HighlightColor else LowlightColor,
                contentDescription = stringResource(R.string.filters),
            )
        }
    )
}

@Composable
private fun CatalogChip(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    UiChip(
        modifier = modifier,
        activated = true,
        text = stringResource(R.string.catalogs),
        highlightColor = DefaultTextColor,
        onClick = onClick,
        icon = {
            UiIcon(
                source = UiIconSource.painter(R.drawable.ic_arrow_down),
                color = DefaultTextColor,
                contentDescription = stringResource(R.string.filters)
            )
        },
    )
}

@UiComponentPreview
@Composable
private fun UiMainFilterPreview() {
    var query by remember { mutableStateOf(QueryUiState()) }
    val genre = fakeGenres().first()

    UiMainFilter(
        query = query,
        onClickFilter = { type ->
            query = when (type) {
                is UiFilterType.Type -> query.copy(type = type.value)
                is UiFilterType.Genre -> {
                    query.copy(genre = if (query.genre == null) genre else null)
                }

                else -> query
            }
        }
    )
}
