package br.dev.singular.overview.presentation.ui.components.style

import androidx.annotation.DrawableRes
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

@Immutable
data class UiImageStyle(
    @get:DrawableRes
    val errorDrawableRes: Int? = null,
    @get:DrawableRes
    val previewDrawableRes: Int? = null,
    val contentScale: ContentScale = ContentScale.FillHeight,
    val shape: Shape = RoundedCornerShape(5.dp),
    val borderStyle: UiBorderStyle = UiBorderStyle(
        visible = false,
        shape = shape
    )
)
