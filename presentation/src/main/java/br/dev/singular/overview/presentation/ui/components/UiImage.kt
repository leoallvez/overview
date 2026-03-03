package br.dev.singular.overview.presentation.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.style.UiBorderStyle
import br.dev.singular.overview.presentation.ui.utils.border
import coil.compose.AsyncImage
import coil.request.ImageRequest

/**
 * A composable that displays an image from a URL.
 *
 * @param url The URL of the image to display.
 * @param modifier The modifier to be applied to the image.
 * @param previewPainter The painter to use in preview mode.
 * @param contentDescription The content description for the image.
 * @param contentScale The scaling algorithm to apply to the image.
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
    @DrawableRes errorDefaultImage: Int = R.drawable.placeholder,
    corner: Dp = dimensionResource(R.dimen.corner_width),
    withBorder: Boolean = false
) {
    val context = LocalContext.current
    val shape = remember(corner) { RoundedCornerShape(corner) }
    var isLoading by remember { mutableStateOf(true) }

    val imageModifier = modifier
        .clip(shape)
        .border(style = UiBorderStyle(visible = withBorder, shape = shape))

    Box(modifier = imageModifier, contentAlignment = Alignment.Center) {
        if (LocalInspectionMode.current && previewPainter != null) {
            isLoading = false
            Image(
                painter = previewPainter,
                contentDescription = contentDescription,
                contentScale = contentScale,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(url)
                    .crossfade(true)
                    .error(errorDefaultImage)
                    .build(),
                contentDescription = contentDescription,
                contentScale = contentScale,
                modifier = Modifier.fillMaxSize(),
                onLoading = { isLoading = true },
                onSuccess = { isLoading = false },
                onError = { isLoading = false }
            )
        }

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .syncedShimmer()
            )
        }
    }
}

@Preview(widthDp = 128, heightDp = 192)
@Composable
internal fun UiImagePreview() {
    UiImage(
        url = "https://imagens.com/movie.jpg",
        contentDescription = "Image description",
        previewPainter = painterResource(R.drawable.sample_poster)
    )
}
