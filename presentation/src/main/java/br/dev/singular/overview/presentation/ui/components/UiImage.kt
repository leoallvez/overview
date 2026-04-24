package br.dev.singular.overview.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.shimmer.UiShimmerBox
import br.dev.singular.overview.presentation.ui.components.style.UiImageStyle
import br.dev.singular.overview.presentation.ui.theme.Surface
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview
import br.dev.singular.overview.presentation.ui.utils.border
import coil.compose.AsyncImage
import coil.request.ImageRequest


/**
 * A composable that displays an image from a URL.
 *
 * @param url The URL of the image to display.
 * @param modifier The modifier to be applied to the image.
 * @param contentDescription The content description for the image.
 * @param style The style configuration for the image (placeholder, scale, corner, etc).
 */
@Composable
fun UiImage(
    url: String?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    style: UiImageStyle = UiImageStyle()
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }

    Box(
        modifier = modifier
            .clip(shape = style.borderStyle.shape)
            .border(
                style = style.borderStyle
                    .copy(visible = style.borderStyle.visible && !isLoading)
            ),
        contentAlignment = Alignment.Center
    ) {
        val previewRes = style.previewDrawableRes
        if (LocalInspectionMode.current && previewRes != null) {
            isLoading = false
            Image(
                painter = painterResource(previewRes),
                contentDescription = contentDescription,
                contentScale = style.contentScale,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(url)
                    .crossfade(true)
                    .apply { style.errorDrawableRes?.let { error(it) } }
                    .build(),
                contentDescription = contentDescription,
                contentScale = style.contentScale,
                modifier = Modifier.fillMaxSize(),
                onLoading = { isLoading = true },
                onSuccess = { isLoading = false },
                onError = { isLoading = false }
            )
        }

        AnimatedVisibility(
            visible = isLoading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            UiShimmerBox(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Surface)
            )
        }
    }
}

@UiComponentPreview
@Composable
private fun UiImagePreview() {
    UiImage(
        url = "https://imagens.com/movie.jpg",
        contentDescription = "Image description",
        style = UiImageStyle(
            errorDrawableRes = R.drawable.error_poster_placeholder,
            previewDrawableRes = R.drawable.sample_poster
        )
    )
}
