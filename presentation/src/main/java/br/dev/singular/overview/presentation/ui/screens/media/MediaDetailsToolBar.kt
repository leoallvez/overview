package br.dev.singular.overview.presentation.ui.screens.media

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.MediaMetadataUiModel
import br.dev.singular.overview.presentation.ui.components.UiImage
import br.dev.singular.overview.presentation.ui.components.UiLikeButton
import br.dev.singular.overview.presentation.ui.components.icon.UiIconButton
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconStyle
import br.dev.singular.overview.presentation.ui.components.style.UiBorderStyle
import br.dev.singular.overview.presentation.ui.components.style.UiImageStyle
import br.dev.singular.overview.presentation.ui.components.text.UiTitle
import br.dev.singular.overview.presentation.ui.theme.Background
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import br.dev.singular.overview.presentation.ui.theme.Surface
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview
import br.dev.singular.overview.presentation.ui.utils.fakeMediaMetadata

/**
 * A specialized toolbar for media details screens.
 * It displays the media backdrop, title, and provides actions like "Back" and "Like".
 *
 * @param model The metadata of the media to be displayed.
 * @param onBack Callback for the back action.
 * @param onLike Callback for the like action.
 */
@Composable
internal fun MediaDetailsToolBar(
    model: MediaMetadataUiModel,
    onBack: () -> Unit,
    onLike: () -> Unit
) {
    Box(Modifier.fillMaxWidth()) {
        with(model) {
            UiImage(
                url = backdropURL,
                contentDescription = title,
                modifier = Modifier
                    .background(Surface)
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(
                        RoundedCornerShape(
                            size = dimensionResource(R.dimen.corner_width)
                        )
                    ),
                style = UiImageStyle(
                    contentScale = ContentScale.Crop,
                    previewDrawableRes = previewDrawableRes,
                    errorDrawableRes = R.drawable.error_backdrop_placeholder,
                )
            )
            ToolbarTitle(
                title = title,
                modifier = Modifier.align(Alignment.BottomStart)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                UiIconButton(
                    iconStyle = UiIconStyle(
                        source = UiIconSource.vector(Icons.AutoMirrored.Filled.KeyboardArrowLeft),
                        descriptionRes = R.string.backstack_icon,
                    ),
                    borderStyle = UiBorderStyle(visible = false),
                    modifier = Modifier.padding(dimensionResource(R.dimen.spacing_4x)),
                    background = Background.copy(alpha = 0.5f),
                    onClick = onBack,
                )
                UiLikeButton(
                    modifier = Modifier.padding(dimensionResource(R.dimen.spacing_4x)),
                    isLiked = isLiked,
                    onClick = onLike
                )
            }
        }
    }
}

@Composable
private fun ToolbarTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.spacing_15x))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Background)
                )
            )
    ) {
        UiTitle(
            text = title,
            color = HighlightColor,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(
                    paddingValues = PaddingValues(
                        horizontal = dimensionResource(R.dimen.spacing_4x)
                    ),
                ),
        )
    }
}

@UiComponentPreview
@Composable
private fun MediaDetailsToolBarPreview() {
    val initialModel = fakeMediaMetadata()
    var uiModel by remember { mutableStateOf(initialModel) }

    MediaDetailsToolBar(
        model = uiModel,
        onBack = {},
        onLike = { uiModel = uiModel.copy(isLiked = !uiModel.isLiked) }
    )
}
