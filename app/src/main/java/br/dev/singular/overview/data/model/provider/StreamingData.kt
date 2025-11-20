package br.dev.singular.overview.data.model.provider

data class StreamingData(
    val selectedId: Long = 0L,
    val list: List<StreamingEntity> = listOf(),
)
