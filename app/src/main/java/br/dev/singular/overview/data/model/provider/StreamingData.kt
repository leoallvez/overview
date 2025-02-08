package br.dev.singular.overview.data.model.provider

data class StreamingData(
    val selectedId: Long = 0L,
    val mains: List<StreamingEntity> = listOf(),
    val others: List<StreamingEntity> = listOf()
) {
    fun hasContent() = mains.isNotEmpty() || others.isNotEmpty()
}
