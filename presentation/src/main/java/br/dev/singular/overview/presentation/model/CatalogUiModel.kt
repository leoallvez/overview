package br.dev.singular.overview.presentation.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import java.util.UUID

@Immutable
data class CatalogUiModel(
    val id: Long,
    val priority: Int,
    val logoURL: String,
    val name: String,
    @get:DrawableRes
    val previewDrawableRes: Int?,
    val uiId: String = UUID.randomUUID().toString(),
)
