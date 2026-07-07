package br.dev.singular.overview.presentation.ui.components.person

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.UiImage
import br.dev.singular.overview.presentation.ui.components.style.UiBorderStyle
import br.dev.singular.overview.presentation.ui.components.style.UiImageStyle
import br.dev.singular.overview.presentation.ui.utils.UiComponentPreview

@Composable
fun UiPersonAvatar(
    url: String,
    modifier: Modifier = Modifier,
    @DrawableRes
    previewDrawableRes: Int? = null,
    size: Dp
) {
    UiImage(
        url = url,
        modifier = modifier
            .size(size)
            .clip(CircleShape),
        style = UiImageStyle(
            errorDrawableRes = R.drawable.error_profile_placeholder,
            previewDrawableRes = previewDrawableRes,
            contentScale = ContentScale.Crop,
            borderStyle = UiBorderStyle(
                visible = true,
                shape = CircleShape
            )
        )
    )
}

@UiComponentPreview
@Composable
internal fun UiPersonAvatarPreview() {
    UiPersonAvatar(
        url = "https://imagens.com/person.jpg",
        modifier = Modifier.padding(dimensionResource(R.dimen.spacing_4x)),
        previewDrawableRes = R.drawable.sample_profile,
        size = 300.dp
    )
}
