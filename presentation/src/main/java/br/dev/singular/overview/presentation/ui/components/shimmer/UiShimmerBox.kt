package br.dev.singular.overview.presentation.ui.components.shimmer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.dev.singular.overview.presentation.R

@Composable
fun UiShimmerBox(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(
        size = dimensionResource(id = R.dimen.spacing_1x)
    )
) {
    Box(
        modifier = modifier
            .clip(shape)
            .shimmer()
    )
}

@Preview
@Composable
internal fun UiShimmerBoxPreview() {
    // Active interactive mode to see the shimmer
    UiShimmerProvider {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            UiShimmerBox(
                modifier = Modifier.size(120.dp)
            )

            UiShimmerBox(
                modifier = Modifier.size(120.dp),
                shape = CircleShape
            )
        }
    }
}
