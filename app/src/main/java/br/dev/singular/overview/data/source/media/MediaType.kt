package br.dev.singular.overview.data.source.media

import androidx.annotation.StringRes
import br.dev.singular.overview.presentation.R


enum class MediaType(val key: String, @StringRes val labelRes: Int, private val order: Int) {
    ALL(key = "all", labelRes = R.string.all, order = 1),
    MOVIE(key = "movie", labelRes = R.string.movies, order = 2),
    TV_SHOW(key = "tv", labelRes = R.string.tv_show, order = 3);

    fun isDefault() = key == ALL.key

    companion object {
        fun getByKey(key: String): MediaType = entries.first { it.key == key }
        fun getAllOrdered(): List<MediaType> = entries.sortedBy { it.order }
    }
}
