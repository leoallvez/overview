package br.dev.singular.overview.presentation.ui.components.streaming

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.StreamingUiModel
import br.dev.singular.overview.presentation.ui.components.icon.UiIcon
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.shimmer.UiShimmerBox
import br.dev.singular.overview.presentation.ui.components.shimmer.UiShimmerProvider
import br.dev.singular.overview.presentation.ui.components.style.UiBorderStyle
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import br.dev.singular.overview.presentation.ui.theme.LowlightColor
import br.dev.singular.overview.presentation.ui.theme.Surface
import br.dev.singular.overview.presentation.ui.utils.border
import br.dev.singular.overview.presentation.ui.utils.fakeStreaming

/**
 * A composable that displays a streaming service item, intended for navigation.
 *
 * @param model The [StreamingUiModel] to display.
 * @param modifier The modifier to be applied to this component.
 * @param onClick The callback to be executed when the item is clicked.
 */
@Composable
fun UiStreamingItem(
    model: StreamingUiModel,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    UiStreamingCard(
        model = model,
        modifier = modifier,
        onClick = onClick,
        rightContent = {
            UiIcon(
                source = UiIconSource.vector(Icons.Default.ChevronRight),
                modifier = Modifier.size(dimensionResource(R.dimen.spacing_9x)),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    )
}

/**
 * A composable that displays a selectable streaming service item.
 *
 * @param selected Whether the item is currently selected.
 * @param model The [StreamingUiModel] to display.
 * @param modifier The modifier to be applied to this component.
 * @param onClick The callback to be executed when the item is clicked.
 */
@Composable
fun UiStreamingItem(
    selected: Boolean,
    model: StreamingUiModel,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    UiStreamingCard(
        model = model,
        modifier = modifier.border(
            style = UiBorderStyle(
                visible = selected,
                color = HighlightColor,
                shape = RoundedCornerShape(
                    size = dimensionResource(R.dimen.corner_width)
                )
            )
        ),
        onClick = onClick,
        rightContent = {
            RadioButton(
                selected = selected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors().copy(
                    selectedColor = HighlightColor,
                    unselectedColor = LowlightColor,
                )
            )
        }
    )
}

/**
 * A skeleton loader for [UiStreamingItem].
 *
 * @param modifier The modifier to be applied to this component.
 */
@Composable
fun UiStreamingItemSkeleton(
    modifier: Modifier = Modifier
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
        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun UiStreamingItemPreview() {
    UiStreamingItem(model = fakeStreaming().first())
}

@Preview(showBackground = true)
@Composable
internal fun UiStreamingItemRadioButtonPreview() {
    var selected by remember { mutableStateOf(false) }
    UiStreamingItem(
        selected = selected,
        model = fakeStreaming().first()
    ) {
        selected = !selected
    }
}

@Preview(showBackground = true)
@Composable
internal fun UiStreamingItemSkeletonPreview() {
    UiShimmerProvider {
        UiStreamingItemSkeleton()
    }
}
