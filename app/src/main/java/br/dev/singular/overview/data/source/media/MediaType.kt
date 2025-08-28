package br.dev.singular.overview.data.source.media

enum class MediaType(val key: String) {
    ALL(key = "all"),
    MOVIE(key = "movie"),
    TV_SHOW(key = "tv");

    fun isDefault() = key == ALL.key

    companion object {
        fun getByKey(key: String): MediaType = entries.first { it.key == key }
    }
}
