package br.dev.singular.overview.presentation.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import java.util.UUID

@Immutable
data class MediaUiModel(
    val id: Long,
    val title: String,
    val type: MediaUiType,
    val posterURL: String,
    val isLiked: Boolean = false,
    @get:DrawableRes
    val previewDrawableRes: Int?,
    val uiId: String = UUID.randomUUID().toString(),
)
