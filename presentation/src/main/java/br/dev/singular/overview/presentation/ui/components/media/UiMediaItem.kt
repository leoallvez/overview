package br.dev.singular.overview.presentation.ui.components.media

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
internal fun UiMediaItem(
    model: MediaUiModel,
    modifier: Modifier = Modifier,
    onClick: (MediaUiModel) -> Unit = {}
) {
    val width = dimensionResource(R.dimen.poster_width)
    val height = dimensionResource(R.dimen.poster_height)

    Column(
        modifier
            .clickable { onClick.invoke(model) }
            .semantics(mergeDescendants = true) {}
    ) {
        UiImage(
            url = model.posterURL,
            previewPainter = model.previewContent,
            withBorder = true,
            modifier = Modifier.size(width, height)
        )
        UiText(
            text = model.title,
            modifier = Modifier
                .width(width)
                .padding(top = dimensionResource(R.dimen.spacing_xs)),
            style = MaterialTheme.typography.bodySmall,
            isBold = true
        )
    }
}

@Preview
@Composable
internal fun UiMediaItemPreview() {
    UiMediaItem(getMediaMocks().first())
}

@Preview(name = "Long Title")
@Composable
internal fun UiMediaWithLongTitlePreview() {
    UiMediaItem(getMediaMocks().first { it.id == 2L })
}
