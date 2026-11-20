package br.dev.singular.overview.presentation.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import java.util.UUID

@Immutable
data class VideoUiModel(
    val id: String,
    val name: String,
    val key: String,
    val thumbnailURL: String,
    @get:DrawableRes
    val previewDrawableRes: Int?,
    val uiId: String = UUID.randomUUID().toString(),
)
