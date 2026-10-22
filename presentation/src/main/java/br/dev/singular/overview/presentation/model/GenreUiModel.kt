package br.dev.singular.overview.presentation.model

import androidx.compose.runtime.Immutable
import java.util.UUID

@Immutable
data class GenreUiModel(
    val id: Long = 0,
    val name: String,
    val uiId: String = UUID.randomUUID().toString(),
)
