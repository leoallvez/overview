package br.dev.singular.overview.presentation.ui.components.icon

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconStyle
import br.dev.singular.overview.presentation.ui.components.style.UiBorderStyle
import br.dev.singular.overview.presentation.ui.theme.Background
import br.dev.singular.overview.presentation.ui.theme.WarningColor
import br.dev.singular.overview.presentation.ui.utils.border

@Composable
fun UiIconButton(
    iconStyle: UiIconStyle,
    modifier: Modifier = Modifier,
    borderStyle: UiBorderStyle = UiBorderStyle(),
    background: Color = Color.White.copy(alpha = 0.1f),
    onClick: () -> Unit
) {
    Box(
        modifier
            .clip(CircleShape)
            .background(background)
            .size(dimensionResource(id = R.dimen.spacing_8x))
            .clickable(onClick = onClick)
            .border(borderStyle)
    ) {
        Box(Modifier.align(Alignment.Center)) {
            iconStyle.apply {
                UiIcon(
                    source = source,
                    contentDescription = descriptionRes?.let { stringResource(it) } ?: "",
                    modifier = iconStyle.modifier.size(dimensionResource(sizeRes)),
                    color = color
                )
            }
        }
    }
}

@Preview
@Composable
fun UiIconButtonVectorPreview() {
    UiIconButton(
        iconStyle = UiIconStyle(
            source = UiIconSource.vector(Icons.AutoMirrored.Filled.KeyboardArrowLeft)
        )
    ) {}
}

@Preview
@Composable
fun UiIconButtonPainterPreview() {
    UiIconButton(
        iconStyle = UiIconStyle(
            source = UiIconSource.painter(R.drawable.ic_arrow_up)
        )
    ) {}
}

@Preview
@Composable
fun UiIconButtonAnimatedPreview() {

    var isLiked by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isLiked) 0.9f else 0.7f,
        tween(durationMillis = 200, easing = LinearEasing),
        label = "PulsatingScale"
    )

    val iconStyle = (if(isLiked) {
        UiIconStyle(
            source = UiIconSource.vector(Icons.Default.Favorite),
            color = WarningColor,
            sizeRes = R.dimen.spacing_8x,
        )
    } else {
        UiIconStyle(
            source = UiIconSource.vector(Icons.Default.FavoriteBorder),
            color = DarkGray,
            sizeRes = R.dimen.spacing_5x,
        )
    }).copy(modifier = Modifier.scale(scale))

    UiIconButton(
        iconStyle = iconStyle,
        borderStyle = UiBorderStyle(
            visible = true,
            color =  if (isLiked) WarningColor else DarkGray,
        ),
        background = Background.copy(alpha = if (isLiked) 0.8f else 0.6f),
    ) {
        isLiked = !isLiked
    }
}
