package br.dev.singular.overview.presentation.ui.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.theme.PrimaryBackground
import br.dev.singular.overview.presentation.ui.utils.border

@Composable
fun BasicImage(
    url: String,
    previewPainter: Painter? = null,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.FillHeight,
    placeholder: Painter = painterResource(R.drawable.placeholder),
    errorDefaultImage: Painter = painterResource(R.drawable.placeholder),
    corner: Dp = dimensionResource(R.dimen.corner),
    withBorder: Boolean = false
) {
    val imageModifier = modifier
        .background(PrimaryBackground)
        .clip(RoundedCornerShape(corner))
        .then(Modifier.border(withBorder))

    if (LocalInspectionMode.current && previewPainter != null) {
        Image(
            painter = previewPainter,
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = imageModifier
        )
    } else {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(data = url)
                .crossfade(true)
                .build(),
            modifier = imageModifier,
            contentScale = contentScale,
            placeholder = placeholder,
            contentDescription = contentDescription,
            error = errorDefaultImage
        )
    }
}

@Preview
@Composable
fun BasicImagePreview() {
    BasicImage(
        url = "https://imagens.com/movie.jpg",
        contentDescription = "Image description",
        previewPainter = painterResource(R.drawable.samper_poster_matrix)
    )
}