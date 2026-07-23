package br.dev.singular.overview.presentation.ui.screens.catalog.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.GenreUiModel
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
import br.dev.singular.overview.presentation.ui.utils.getColor
import br.dev.singular.overview.presentation.ui.utils.getImageVector
import br.dev.singular.overview.presentation.ui.utils.localizedName
import com.composables.icons.lucide.ChevronDown
import com.composables.icons.lucide.Lucide

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
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onClickFilter: (UiFilterType) -> Unit = {}
) {
    val isAll = query.type == MediaUiType.ALL

    UiAnimatedVisibility(visible = visible) {
        LazyRow(
            modifier = modifier,
            contentPadding = contentPadding,
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
                        genre = query.genre,
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
    genre: GenreUiModel?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isActivated = genre != null
    val label = genre?.localizedName() ?: stringResource(R.string.genre)
    val highlightColor = genre?.getColor() ?: HighlightColor

    UiChip(
        modifier = modifier,
        text = label,
        onClick = onClick,
        activated = isActivated,
        highlightColor = highlightColor,
        icon = {
            val iconSource = if (genre != null) {
                UiIconSource.vector(genre.getImageVector())
            } else {
                UiIconSource.vector(Lucide.ChevronDown)
            }

            UiIcon(
                source = iconSource,
                color = if (isActivated) highlightColor else LowlightColor,
                contentDescription = if (isActivated) null else stringResource(R.string.filter_by_genre),
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
                source = UiIconSource.vector(icon = Lucide.ChevronDown),
                color = DefaultTextColor,
                contentDescription = stringResource(R.string.filters)
            )
        },
    )
}

@UiComponentPreview
@Composable
internal fun UiMainFilterPreview() {
    val queryState = remember { mutableStateOf(QueryUiState()) }
    val genre = fakeGenres().first()

    UiMainFilter(
        query = queryState.value,
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_4x)),
        onClickFilter = { type ->
            queryState.value = when (type) {
                is UiFilterType.Type -> queryState.value.copy(type = type.value)
                is UiFilterType.Genre -> {
                    queryState.value.copy(genre = if (queryState.value.genre == null) genre else null)
                }

                else -> queryState.value
            }
        }
    )
}
