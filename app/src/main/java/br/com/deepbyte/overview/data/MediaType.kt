package br.com.deepbyte.overview.data

import androidx.annotation.StringRes
import br.com.deepbyte.overview.R

enum class MediaType(val key: String, @StringRes val labelRes: Int) {
    MOVIE(key = "movie", labelRes = R.string.movies),
    TV_SHOW(key = "tv", labelRes = R.string.tv_show),
    ALL(key = "all", labelRes = R.string.all)
}
