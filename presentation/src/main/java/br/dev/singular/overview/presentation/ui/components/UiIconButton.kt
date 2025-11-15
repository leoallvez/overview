package br.dev.singular.overview.presentation.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.theme.Background
import br.dev.singular.overview.presentation.ui.theme.DefaultTextColor
import br.dev.singular.overview.presentation.ui.theme.HighlightColor
import br.dev.singular.overview.presentation.ui.theme.WarningColor
import br.dev.singular.overview.presentation.ui.utils.border

@Composable
fun UiIconButton(
    icon: Any,
    modifier: Modifier = Modifier,
    iconColor: Color = DefaultTextColor,
    iconSize: Dp = dimensionResource(R.dimen.spacing_8x),
    iconModifier: Modifier = Modifier,
    iconDescription: String? = null,
    showBorder: Boolean = true,
    borderColor: Color = DarkGray,
    borderWidth: Dp = dimensionResource(R.dimen.border_width),
    background: Color = Color.White.copy(alpha = 0.1f),
    onClick: () -> Unit = {}
) {
    Box(
        modifier
            .clip(CircleShape)
            .background(background)
            .size(dimensionResource(id = R.dimen.spacing_8x))
            .clickable(onClick = onClick)
            .border(
                isVisible = showBorder,
                width = borderWidth,
                color = borderColor,
                shape = CircleShape
            )
    ) {
        Box(Modifier.align(Alignment.Center)) {
            UiIcon(
                icon = icon,
                contentDescription = iconDescription,
                modifier = iconModifier.size(iconSize),
                color = iconColor
            )
        }
    }
}

@Preview
@Composable
fun UiIconButtonPreview() {

    var isLiked by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isLiked) 0.9f else 0.7f,
        tween(durationMillis = 200, easing = LinearEasing),
        label = "PulsatingScale"
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        UiIconButton(
            icon = Icons.AutoMirrored.Filled.KeyboardArrowLeft
        )
        UiIconButton(
            icon = painterResource(id = R.drawable.ic_arrow_down),
            iconColor = HighlightColor,
        )
        UiIconButton(
            icon = painterResource(id = R.drawable.ic_arrow_up)
        )
        UiIconButton(
            icon = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            background = Color.White.copy(alpha = 0.1f),
            showBorder = false
        )
        UiIconButton(
            icon = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            background = Background.copy(alpha = if (isLiked) 0.8f else 0.6f),
            borderColor = if (isLiked) WarningColor else DarkGray,
            iconSize = if (isLiked) dimensionResource(R.dimen.spacing_7x) else dimensionResource(R.dimen.spacing_5x),
            iconColor = if (isLiked) WarningColor else DarkGray,
            iconModifier = Modifier.scale(scale),
            showBorder = true
        ) {
            isLiked = !isLiked
        }


    }
}
