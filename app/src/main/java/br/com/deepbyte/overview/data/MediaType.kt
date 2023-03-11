package br.com.deepbyte.overview.data

import androidx.annotation.StringRes
import br.com.deepbyte.overview.R

enum class MediaType(val key: String, @StringRes val labelRes: Int, private val order: Int) {
    ALL(key = "all", labelRes = R.string.all, order = 1),
    MOVIE(key = "movie", labelRes = R.string.movies, order = 2),
    TV_SHOW(key = "tv", labelRes = R.string.tv_show, order = 3);

    companion object {
        fun getByKey(key: String): MediaType = values().first { it.key == key }
        fun getAllOrdered(): List<MediaType> = values().sortedBy { it.order }
    }
}
