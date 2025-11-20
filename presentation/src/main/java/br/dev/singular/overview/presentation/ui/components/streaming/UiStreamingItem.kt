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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.StreamingUiModel
import br.dev.singular.overview.presentation.ui.components.UiImage
import br.dev.singular.overview.presentation.ui.components.icon.UiIcon
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.text.UiTitle
import br.dev.singular.overview.presentation.ui.theme.Surface
import br.dev.singular.overview.presentation.ui.utils.getStreamingMocks

@Composable
fun UiStreamingItem(
    model: StreamingUiModel,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.spacing_18x))
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Surface
        ),
        shape = RoundedCornerShape(dimensionResource(R.dimen.border_width)),
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
                previewPainter = model.previewContent,
                errorDefaultImage = R.drawable.launcher_playstore,
                placeholder = R.drawable.launcher_playstore,
                modifier = Modifier.size(dimensionResource(R.dimen.spacing_12x))
            )

            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_3x)))

            UiTitle(
                text = model.name,
                modifier = Modifier.weight(1f),
            )

            UiIcon(
                source = UiIconSource.vector(Icons.Default.ChevronRight),
                modifier = Modifier.size(dimensionResource(R.dimen.spacing_9x)),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UiStreamingItemPreview() {
    UiStreamingItem(model = getStreamingMocks().first())
}
