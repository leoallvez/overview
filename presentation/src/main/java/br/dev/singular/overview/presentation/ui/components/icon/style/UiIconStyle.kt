package br.dev.singular.overview.presentation.ui.components.icon.style

import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.theme.DefaultTextColor

data class UiIconStyle(
    val source: UiIconSource,
    val modifier: Modifier = Modifier,
    val color: Color = DefaultTextColor,
    @param:DimenRes val sizeRes: Int = R.dimen.spacing_8x,
    @param:StringRes val descriptionRes: Int? = null,
)
