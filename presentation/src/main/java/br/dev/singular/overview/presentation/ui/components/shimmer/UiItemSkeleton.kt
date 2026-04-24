package br.dev.singular.overview.presentation.ui.components.shimmer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.theme.Surface
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview

/**
 * A skeleton loader for items that use a card layout with left icon, center text and optional right content.
 *
 * @param modifier The modifier to be applied to this component.
 * @param showRightShimmer Whether to show a shimmer box on the right side.
 */
@Composable
fun UiItemSkeleton(
    modifier: Modifier = Modifier,
    showRightShimmer: Boolean = true
) {
    val shape = RoundedCornerShape(dimensionResource(R.dimen.corner_width))
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.spacing_18x)),
        colors = CardDefaults.cardColors(
            containerColor = Surface
        ),
        shape = shape,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(R.dimen.spacing_3x)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            UiShimmerBox(
                modifier = Modifier.size(dimensionResource(R.dimen.spacing_12x)),
                shape = shape
            )

            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_3x)))

            UiShimmerBox(
                modifier = Modifier
                    .weight(1f)
                    .height(dimensionResource(R.dimen.spacing_4x))
            )

            if (showRightShimmer) {
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_3x)))

                UiShimmerBox(
                    modifier = Modifier.size(dimensionResource(R.dimen.spacing_9x)),
                    shape = shape
                )
            }
        }
    }
}

@UiComponentPreview
@Composable
private fun UiItemSkeletonPreview() {
    UiShimmerProvider {
        UiItemSkeleton()
    }
}
