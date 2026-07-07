package br.dev.singular.overview.presentation.ui.components.person

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.PersonUiModel
import br.dev.singular.overview.presentation.ui.components.shimmer.UiShimmerBox
import br.dev.singular.overview.presentation.ui.components.shimmer.UiShimmerProvider
import br.dev.singular.overview.presentation.ui.components.text.UiTitle
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview
import br.dev.singular.overview.presentation.ui.utils.fakePeople
import br.dev.singular.overview.presentation.ui.utils.rememberMaxHeightState
import br.dev.singular.overview.presentation.ui.utils.syncMaxHeight
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

/**
 * A composable that displays a horizontal list of person items with a title.
 *
 * @param title The title to be displayed above the list.
 * @param modifier The modifier to be applied to this component.
 * @param people The immutable list of [PersonUiModel] to be displayed.
 * @param contentPadding The padding to be applied to the content.
 * @param onClick The callback to be executed when a person item is clicked.
 */
@Composable
fun UiPersonList(
    title: String,
    modifier: Modifier = Modifier,
    people: ImmutableList<PersonUiModel>,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onClick: (id: Long) -> Unit = {}
) {
    if (people.isNotEmpty()) {
        val maxHeightState = rememberMaxHeightState()

        Column(
            modifier = modifier.animateContentSize()
        ) {
            UiTitle(
                text = title,
                modifier = Modifier.padding(contentPadding)
            )
            LazyRow(
                contentPadding = contentPadding,
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_2x))
            ) {
                items(
                    items = people,
                    key = { it.uiId },
                    contentType = { "person" }
                ) { model ->
                    UiPersonItem(
                        model = model,
                        onClick = onClick,
                        modifier = Modifier.syncMaxHeight(maxHeightState)
                    )
                }
            }
        }
    }
}

/**
 * A skeleton placeholder for [UiPersonList] to be used during loading states.
 *
 * @param modifier The modifier to be applied to this component.
 * @param contentPadding The padding to be applied to the content.
 * @param itemCount The number of skeleton items to display in the list.
 */
@Composable
fun UiPersonListSkeleton(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    itemCount: Int = 10,
) {
    Column(modifier = modifier) {
        UiShimmerBox(
            modifier = Modifier
                .width(150.dp)
                .height(dimensionResource(R.dimen.spacing_8x))
                .padding(contentPadding)
                .padding(vertical = dimensionResource(R.dimen.spacing_1x))
        )
        LazyRow(
            contentPadding = contentPadding,
            horizontalArrangement = Arrangement
                .spacedBy(dimensionResource(R.dimen.spacing_2x))
        ) {
            items(itemCount) {
                UiPersonItemSkeleton()
            }
        }
    }
}

@UiComponentPreview
@Composable
internal fun UiPersonListPreview() {
    UiPersonList(
        title = stringResource(R.string.cast),
        people = fakePeople(),
    )
}

@UiComponentPreview
@Composable
internal fun UiPersonListWithContentPaddingPreview() {
    UiPersonList(
        title = stringResource(R.string.cast),
        people = fakePeople(),
        contentPadding = PaddingValues(dimensionResource(R.dimen.spacing_4x)),
    )
}

@UiComponentPreview
@Composable
internal fun UiPersonListEmptyPreview() {
    UiPersonList(
        title = stringResource(R.string.cast),
        people = emptyList<PersonUiModel>().toImmutableList(),
    )
}

@UiComponentPreview
@Composable
internal fun UiPersonListSkeletonPreview() {
    UiShimmerProvider {
        UiPersonListSkeleton()
    }
}
