package br.dev.singular.overview.presentation.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import java.util.UUID

@Immutable
data class PersonUiModel(
    val id: Long,
    val name: String,
    val description: String,
    val profileURL: String,
    @get:DrawableRes
    val previewDrawableRes: Int?,
    val uiId: String = UUID.randomUUID().toString(),
)
