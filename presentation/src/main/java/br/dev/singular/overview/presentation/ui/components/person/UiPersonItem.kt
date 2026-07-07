package br.dev.singular.overview.presentation.ui.components.person

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.PersonUiModel
import br.dev.singular.overview.presentation.ui.components.shimmer.UiShimmerBox
import br.dev.singular.overview.presentation.ui.components.text.UiText
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview
import br.dev.singular.overview.presentation.ui.utils.fakePerson

/**
 * A UI component that displays a person item, typically used in lists or grids of cast/crew.
 * It shows the person's avatar, name, and their character/job.
 *
 * @param model The [PersonUiModel] containing the data to be displayed.
 * @param modifier The [Modifier] to be applied to this item.
 * @param onClick A callback to be invoked when the item is clicked.
 */
@Composable
fun UiPersonItem(
    model: PersonUiModel,
    modifier: Modifier = Modifier,
    onClick: (id: Long) -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(dimensionResource(R.dimen.avatar_medium))
            .heightIn(max = dimensionResource(R.dimen.person_item_height))
            .clickable { onClick(model.id) },
        verticalArrangement = Arrangement
                .spacedBy(dimensionResource(R.dimen.spacing_1x)),

    ) {
        UiPersonAvatar(
            url = model.profileURL,
            previewDrawableRes = model.previewDrawableRes,
            size = dimensionResource(R.dimen.avatar_medium),
        )
        UiText(
            text = model.name,
            isBold = true,
            maxLines = 2
        )
        UiText(
            modifier = Modifier
                .padding(bottom = dimensionResource(R.dimen.spacing_1x)),
            text = model.description,
            style = MaterialTheme.typography.bodySmall,
            color = HighlightColor,
            maxLines = 2
        )
    }
}

@UiComponentPreview
@Composable
internal fun UiPersonItemPreview() {
    UiPersonItem(
        model = fakePerson(
            name = stringResource(R.string.lorem_ipsum_short),
            description = stringResource(R.string.lorem_ipsum_short)
        ),
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_2x))
    )
}

@UiComponentPreview
@Composable
internal fun UiPersonItemLongTextPreview() {
    UiPersonItem(
        model = fakePerson(
            name = stringResource(R.string.lorem_ipsum_long),
            description = stringResource(R.string.lorem_ipsum_long)
        ),
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_2x))
    )
}

/**
 * A skeleton placeholder for [UiPersonItem] to be used during loading states.
 *
 * @param modifier The [Modifier] to be applied to this skeleton.
 */
@Composable
fun UiPersonItemSkeleton(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(dimensionResource(R.dimen.avatar_medium))
            .height(dimensionResource(R.dimen.person_item_height)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement
            .spacedBy(dimensionResource(R.dimen.spacing_1x)),
    ) {
        UiShimmerBox(
            modifier = Modifier.size(dimensionResource(R.dimen.avatar_medium)),
            shape = CircleShape
        )
        UiShimmerBox(
            modifier = Modifier
                .size(
                    width = dimensionResource(R.dimen.spacing_18x),
                    height = dimensionResource(R.dimen.spacing_4x)
                )
        )
        UiShimmerBox(
            modifier = Modifier
                .size(
                    width = dimensionResource(R.dimen.spacing_15x),
                    height = dimensionResource(R.dimen.spacing_4x)
                )
                .padding(bottom = dimensionResource(R.dimen.spacing_1x))
        )
    }
}

@UiComponentPreview
@Composable
internal fun UiPersonItemSkeletonPreview() {
    UiPersonItemSkeleton(
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_2x))
    )
}
