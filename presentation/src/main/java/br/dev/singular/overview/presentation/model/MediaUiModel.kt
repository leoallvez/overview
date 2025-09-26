package br.dev.singular.overview.presentation.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter

data class MediaUiModel(
    val id: Long,
    val title: String,
    val type: MediaUiType,
    val posterURL: String,
    var isLiked: Boolean = false,
    val previewContent: Painter = ColorPainter(Color.Gray),
)
