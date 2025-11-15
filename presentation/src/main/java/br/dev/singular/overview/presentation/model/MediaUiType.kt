package br.dev.singular.overview.presentation.model

import androidx.annotation.StringRes
import br.dev.singular.overview.presentation.R

enum class MediaUiType(@param:StringRes val labelRes: Int) {
    ALL(R.string.all),
    MOVIE(R.string.movies),
    TV(R.string.tv_show);

    companion object {
        fun getByName(name: String) = entries.first { it.name.lowercase() == name }
    }
}
