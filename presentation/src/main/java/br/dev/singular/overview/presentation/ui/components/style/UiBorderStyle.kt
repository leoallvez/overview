package br.dev.singular.overview.presentation.ui.components.style

import androidx.annotation.DimenRes
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import br.dev.singular.overview.presentation.R

data class UiBorderStyle(
    val visible: Boolean = true,
    val color: Color = DarkGray,
    @param:DimenRes val width: Int = R.dimen.border_width,
    val shape: RoundedCornerShape = CircleShape
)
