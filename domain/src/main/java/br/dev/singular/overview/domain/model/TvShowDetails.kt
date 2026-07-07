package br.dev.singular.overview.domain.model

data class TvShowDetails(
    val id: Long,
    val name: String,
    val numberOfSeasons: Int,
    val numberOfEpisodes: Int,
    val episodeRuntime: List<Int>,
    val firstAirDate: String,
    val posterPath: String,
    val backdropPath: String,
    val overview: String,
    val credits: Credits,
    val genres: List<Genre>,
    val creators: List<String>,
    val videos: List<Video>,
    val catalogs: List<Catalog>,
    val similar: List<Media>,
)
