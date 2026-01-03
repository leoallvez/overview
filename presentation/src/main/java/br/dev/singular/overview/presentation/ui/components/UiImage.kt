package br.dev.singular.overview.presentation.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
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
import br.dev.singular.overview.presentation.ui.components.style.UiBorderStyle
import br.dev.singular.overview.presentation.ui.utils.border

/**
 * A composable that displays an image from a URL.
 *
 * @param url The URL of the image to display.
 * @param modifier The modifier to be applied to the image.
 * @param previewPainter The painter to use in preview mode.
 * @param contentDescription The content description for the image.
 * @param contentScale The scaling algorithm to apply to the image.
 * @param placeholder The drawable resource to use as a placeholder.
 * @param errorDefaultImage The drawable resource to use if the image fails to load.
 * @param corner The corner radius of the image.
 * @param withBorder Whether to draw a border around the image.
 */
@Composable
fun UiImage(
    url: String,
    modifier: Modifier = Modifier,
    previewPainter: Painter? = null,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.FillHeight,
    @DrawableRes placeholder: Int = R.drawable.placeholder,
    @DrawableRes errorDefaultImage: Int = R.drawable.placeholder,
    corner: Dp = dimensionResource(R.dimen.corner_width),
    withBorder: Boolean = false
) {
    val shape = RoundedCornerShape(corner)
    val imageModifier = modifier
        .clip(shape)
        .then(Modifier
            .border(style = UiBorderStyle(visible = withBorder, shape =  shape)))

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
                .data(url)
                .crossfade(true)
                .placeholder(placeholder)
                .error(errorDefaultImage)
                .build(),
            contentDescription = contentDescription,
            modifier = imageModifier,
            contentScale = contentScale
        )
    }
}

@Preview
@Composable
internal fun UiImagePreview() {
    UiImage(
        url = "https://imagens.com/movie.jpg",
        contentDescription = "Image description",
        previewPainter = painterResource(R.drawable.sample_poster)
    )
}
