package br.dev.singular.overview.data.model.provider

class StreamingData(
    val selected: List<StreamingEntity> = listOf(),
    val unselected: List<StreamingEntity> = listOf()
) {
    fun isNotEmpty() = selected.isNotEmpty() || unselected.isNotEmpty()
}
