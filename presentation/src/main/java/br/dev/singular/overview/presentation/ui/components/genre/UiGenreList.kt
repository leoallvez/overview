package br.dev.singular.overview.presentation.ui.components.genre

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.GenreUiModel
import br.dev.singular.overview.presentation.ui.components.UiChip
import br.dev.singular.overview.presentation.ui.components.icon.UiIcon
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.shimmer.UiShimmerBox
import br.dev.singular.overview.presentation.ui.components.shimmer.UiShimmerProvider
import br.dev.singular.overview.presentation.ui.theme.DisabledHighlightColor
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview
import br.dev.singular.overview.presentation.ui.utils.defaultBackground
import br.dev.singular.overview.presentation.ui.utils.fakeGenres
import br.dev.singular.overview.presentation.ui.utils.getImageVector
import br.dev.singular.overview.presentation.ui.utils.localizedName
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

/**
 * A composable that displays a horizontal list of genre items.
 *
 * @param genres The immutable list of [GenreUiModel] to be displayed.
 * @param modifier The modifier to be applied to this component.
 * @param contentPadding The padding to be applied to the content.
 * @param onClick The callback to be executed when a genre item is clicked.
 */
@Composable
fun UiGenreList(
    genres: ImmutableList<GenreUiModel>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onClick: (Long) -> Unit = {}
) {
    if (genres.isNotEmpty()) {
        LazyRow(
            modifier = modifier,
            horizontalArrangement = Arrangement
                .spacedBy(dimensionResource(R.dimen.spacing_1x)),
            contentPadding = contentPadding
        ) {
            items(
                items = genres,
                key = { it.uiId },
                contentType = { "genre" }
            ) { genre ->
                UiGenreItem(genre, onClick)
            }
        }
    }
}

@Composable
private fun UiGenreItem(genre: GenreUiModel, onClick: (Long) -> Unit) {
    val color = DisabledHighlightColor
    UiChip(
        text = genre.localizedName(),
        shape = RoundedCornerShape(percent = 0),
        highlightColor = color,
        lowlightColor = color,
        onClick = { onClick(genre.id) },
        icon = {
            UiIcon(
                color = color,
                source = UiIconSource.UiVector(
                    imageVector = genre.getImageVector()
                )
            )
        }
    )
}

/**
 * A skeleton loader for [UiGenreList].
 *
 * @param modifier The modifier to be applied to this component.
 * @param contentPadding The padding to be applied to the content.
 */
@Composable
fun UiGenreListSkeleton(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_1x)),
        contentPadding = contentPadding,
        userScrollEnabled = false
    ) {
        items(5) {
            UiGenreItemSkeleton()
        }
    }
}

@Composable
private fun UiGenreItemSkeleton() {
    UiShimmerBox(
        modifier = Modifier
            .width(dimensionResource(R.dimen.spacing_20x))
            .height(dimensionResource(R.dimen.spacing_7x)),
        shape = RoundedCornerShape(percent = 0)
    )
}

@UiComponentPreview
@Composable
internal fun UiGenreListPreview() {
    UiGenreList(
        genres = fakeGenres().take(3).toImmutableList()
    )
}

@UiComponentPreview
@Composable
internal fun UiGenreListWithContentPaddingPreview() {
    UiGenreList(
        contentPadding = PaddingValues(start = dimensionResource(R.dimen.spacing_4x)),
        genres = fakeGenres().take(3).toImmutableList()
    )
}

@UiComponentPreview
@Composable
internal fun UiGenreListWithBackgroundPreview() {
    UiGenreList(
        modifier = Modifier.defaultBackground(),
        genres = fakeGenres().take(3).toImmutableList()
    )
}

@UiComponentPreview
@Composable
internal fun UiGenreListEmptyPreview() {
    UiGenreList(
        modifier = Modifier.defaultBackground(),
        genres = emptyList<GenreUiModel>().toImmutableList()
    )
}

@UiComponentPreview
@Composable
internal fun UiGenreListSkeletonPreview() {
    UiShimmerProvider {
        UiGenreListSkeleton(
            modifier = Modifier.defaultBackground(),
            contentPadding = PaddingValues(start = dimensionResource(R.dimen.spacing_4x))
        )
    }
}
