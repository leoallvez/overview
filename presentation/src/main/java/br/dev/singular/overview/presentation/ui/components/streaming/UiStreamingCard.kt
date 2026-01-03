package br.dev.singular.overview.presentation.ui.components.streaming

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.StreamingUiModel
import br.dev.singular.overview.presentation.ui.components.UiImage
import br.dev.singular.overview.presentation.ui.components.style.UiBorderStyle
import br.dev.singular.overview.presentation.ui.components.text.UiTitle
import br.dev.singular.overview.presentation.ui.theme.Surface
import br.dev.singular.overview.presentation.ui.utils.border

/**
 * A composable that displays a card for a streaming service.
 *
 * @param model The [StreamingUiModel] containing the data to be displayed.
 * @param modifier The modifier to be applied to this component.
 * @param onClick The callback to be executed when the card is clicked.
 * @param rightContent A composable to be displayed on the right side of the card.
 */
@Composable
internal fun UiStreamingCard(
    model: StreamingUiModel,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    rightContent: @Composable ()-> Unit
) {
    val shape = RoundedCornerShape(dimensionResource(R.dimen.corner_width))
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.spacing_18x))
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Surface
        ),
        shape = shape,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = modifier.fillMaxSize()
                .padding(horizontal = dimensionResource(R.dimen.spacing_3x)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            UiImage(
                url = model.logoURL,
                modifier = Modifier
                    .size(dimensionResource(R.dimen.spacing_12x))
                    .border(style = UiBorderStyle(shape = shape)),
                previewPainter = model.previewContent,
                errorDefaultImage = R.drawable.launcher_playstore,
                contentScale = ContentScale.FillWidth
            )

            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_3x)))

            UiTitle(
                text = model.name,
                modifier = Modifier.weight(1f),
            )
            rightContent()
        }
    }
}
