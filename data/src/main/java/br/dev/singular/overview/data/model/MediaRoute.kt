package br.dev.singular.overview.data.model

enum class MediaRoute(
    val key: String,
    val path: String
) {
    ALL_TRENDING("all_trending", "trending/all/day"),
    TV_POPULAR("tv_popular", "tv/top_rated"),
    TV_TRENDING("tv_trending", "trending/tv/day"),
    MOVIE_POPULAR("movie_popular", "movie/top_rated"),
    MOVIE_DISCOVER("movie_discover", "discover/movie"),
    MOVIE_TRENDING("movie_trending", "trending/movie/day"),
    MOVIE_TOP_RATED("movie_top_rated", "movie/top_rated"),
    UNKNOWN("unknown", "unknown");

    companion object {
        private val map = entries.associateBy { it.key }
        fun getByKey(key: String) = map[key] ?: UNKNOWN
    }
}
