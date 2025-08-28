package br.dev.singular.overview.presentation.model

data class MediaUiParam(
    val key: String = "",
    val type: MediaUiType = MediaUiType.ALL,
    val isLiked: Boolean = false,
    val query: String = "",
    val page: Int = 0,
    private val refreshKey: Int = 0
) {
    fun refresh() = copy(refreshKey = refreshKey + 1)
}
