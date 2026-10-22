package br.dev.singular.overview.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
data class ScrollUiState(
    val index: Int = 0,
    val offset: Int = 0
)
