package br.dev.singular.overview.domain.model

data class MovieDetails(
    val id: Long,
    val title: String,
    val releaseDate: String,
    val runtime: Int,
    val posterPath: String,
    val backdropPath: String,
    val overview: String,
    val credits: Credits,
    val genres: List<Genre>,
    val directors: List<String>,
    val videos: List<Video>,
    val catalogs: List<Catalog>,
    val similar: List<Media>,
)
