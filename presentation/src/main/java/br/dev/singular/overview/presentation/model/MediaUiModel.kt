package br.dev.singular.overview.presentation.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import java.util.UUID

@Immutable
data class MediaUiModel(
    val id: Long,
    val title: String,
    val type: MediaUiType,
    val posterURL: String,
    val isLiked: Boolean = false,
    val previewContent: Painter = ColorPainter(Color.Gray),
    val uiId: String = UUID.randomUUID().toString(),
)
