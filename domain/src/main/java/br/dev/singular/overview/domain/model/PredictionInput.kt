package br.dev.singular.overview.domain.model

data class PredictionInput(
    val id: Long,
    val type: MediaType,
    val title: String,
    val genres: List<String>,
    val favorites: List<Media> = emptyList(),
    val favoriteGenres: Map<String, Int> = emptyMap()
)
