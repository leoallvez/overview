package br.dev.singular.overview.presentation.ui.media

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
import br.dev.singular.overview.presentation.model.MediaUIModel
import br.dev.singular.overview.presentation.ui.image.BasicImage
import br.dev.singular.overview.presentation.ui.text.BasicText
import br.dev.singular.overview.presentation.ui.utils.getMediaMocks

@Composable
fun MediaItem(
    model: MediaUIModel,
    modifier: Modifier = Modifier,
    onClick: (MediaUIModel) -> Unit = {}
) {
    val width = dimensionResource(R.dimen.poster_width)
    val height = dimensionResource(R.dimen.poster_height)

    Column(
        modifier
            .clickable { onClick.invoke(model) }
            .semantics(mergeDescendants = true) {}
    ) {
        BasicImage(
            url = model.posterURLPath,
            previewPainter = model.previewContent,
            withBorder = true,
            modifier = Modifier.size(width, height)
        )
        BasicText(
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
fun MediaPosterPreview() {
    MediaItem(getMediaMocks().first())
}

@Preview(name = "Long Title")
@Composable
fun MediaPosterWithLongTitlePreview() {
    MediaItem(getMediaMocks().first { it.id == 2L })
}
