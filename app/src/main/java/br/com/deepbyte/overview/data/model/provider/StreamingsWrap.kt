package br.com.deepbyte.overview.data.model.provider

class StreamingsWrap(
    val selected: List<Streaming>,
    val unselected: List<Streaming>
) {
    fun isNotEmpty() = selected.isNotEmpty() || unselected.isNotEmpty()
}
