package br.dev.singular.overview.presentation.model

import kotlinx.collections.immutable.ImmutableList

data class SelectStreamingUiModel(
    val selectedId: Long?,
    val streaming: ImmutableList<StreamingUiModel>
)
