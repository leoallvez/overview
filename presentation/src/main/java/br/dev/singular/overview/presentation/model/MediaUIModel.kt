package br.dev.singular.overview.presentation.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter

data class MediaUIModel(
    val id: Long,
    val title: String,
    val type: MediaUIType,
    val posterURLPath: String,
    val previewContent: Painter = ColorPainter(Color.Gray),
)
