package br.com.deepbyte.overview.data.model.provider

class StreamingsWrap(
    val selected: List<StreamingEntity>,
    val unselected: List<StreamingEntity>
) {
    fun isNotEmpty() = selected.isNotEmpty() || unselected.isNotEmpty()
}
