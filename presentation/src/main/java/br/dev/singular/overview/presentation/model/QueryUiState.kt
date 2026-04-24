package br.dev.singular.overview.presentation.model

data class QueryUiState(
    val key: String = "",
    val type: MediaUiType = MediaUiType.ALL,
    val isLiked: Boolean = false,
    val query: String = "",
    val page: Int = 0,
    val genre: GenreUiModel? = null,
    val catalog: CatalogUiModel? = null,
    val refreshKey: Int = 0
) {
    fun refresh() = copy(refreshKey = refreshKey + 1)
}
