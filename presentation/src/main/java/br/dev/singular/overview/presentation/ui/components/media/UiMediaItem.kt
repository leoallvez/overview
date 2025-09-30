package br.dev.singular.overview.presentation.ui.components.media

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.ui.components.UiImage
import br.dev.singular.overview.presentation.ui.components.text.UiText
import br.dev.singular.overview.presentation.ui.utils.getMediaMocks

@Composable
fun UiMediaItem(
    model: MediaUiModel,
    modifier: Modifier = Modifier,
    onClick: (MediaUiModel) -> Unit = {}
) {
    val width = dimensionResource(R.dimen.poster_width)
    val height = dimensionResource(R.dimen.poster_height)

    Column(
        Modifier
            .clickable { onClick.invoke(model) }
            .semantics(mergeDescendants = true) {}
            .width(width)
    ) {
        UiImage(
            url = model.posterURL,
            previewPainter = model.previewContent,
            withBorder = true,
            modifier = modifier.size(width, height)
        )
        UiText(
            text = model.title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.spacing_1x)),
            style = MaterialTheme.typography.bodySmall,
            isBold = true
        )
    }
}

@Preview(name = "Short Title")
@Composable
internal fun UiMediaItemPreview() {
    UiMediaItem(getMediaMocks().first())
}

@Preview(name = "Long Title")
@Composable
internal fun UiMediaWithLongTitlePreview() {
    UiMediaItem(getMediaMocks().first { it.title.length > 20 })
}
