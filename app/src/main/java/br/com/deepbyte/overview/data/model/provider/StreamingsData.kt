package br.com.deepbyte.overview.data.model.provider

class StreamingsData(
    val selected: List<StreamingEntity> = listOf(),
    val unselected: List<StreamingEntity> = listOf()
) {
    fun isNotEmpty() = selected.isNotEmpty() || unselected.isNotEmpty()
}
