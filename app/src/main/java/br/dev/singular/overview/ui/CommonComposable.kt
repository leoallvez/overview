package br.dev.singular.overview.ui

//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.dev.singular.overview.data.model.provider.StreamingEntity
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.UiImage
import br.dev.singular.overview.presentation.ui.components.style.UiBorderStyle
import br.dev.singular.overview.presentation.ui.components.style.UiImageStyle
import br.dev.singular.overview.util.onClick

@Composable
fun StreamingIcon(
    modifier: Modifier = Modifier,
    streaming: StreamingEntity?,
    size: Dp = 48.dp,
    hasBorder: Boolean,
    corner: Dp = dimensionResource(id = R.dimen.corner_width),
    onClick: (() -> Unit)? = null
) {
    streaming?.let {
        UiImage(
            url = streaming.getLogoImage(),
            contentDescription = streaming.name,
            style = UiImageStyle(
                errorDrawableRes = R.drawable.error_catalog_logo_placeholder,
                shape = RoundedCornerShape(size = corner),
                borderStyle = UiBorderStyle(visible = hasBorder)
            ),
            modifier = modifier
                .size(size)
                .onClick(action = onClick)
        )
    }
}
