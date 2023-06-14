package br.com.deepbyte.overview.data.model

import android.os.Parcelable
import br.com.deepbyte.overview.util.joinToStringWithComma
import kotlinx.parcelize.Parcelize
import br.com.deepbyte.overview.data.source.media.MediaTypeEnum as MediaType

@Parcelize
class Filters(
    var mediaType: MediaType = MediaType.ALL,
    var streamingsIds: List<Long> = emptyList(),
    private val genresIds: MutableList<Long> = mutableListOf()
) : Parcelable {

    fun hasGenreWithId(genreId: Long) = genresIds.any { it == genreId }

    fun updateGenreIds(genreId: Long) {
        val hasGenre = hasGenreWithId(genreId)
        if (hasGenre) {
            genresIds.remove(genreId)
        } else {
            genresIds.add(genreId)
        }
    }

    fun getGenreIdsSeparatedWithComma() = genresIds.joinToStringWithComma()

    fun genresIsIsNotEmpty() = genresIds.isEmpty().not()

    fun clearGenresIds() = genresIds.clear()

    fun genreQuantity(): String = if (genresIds.isNotEmpty()) {
        genresIds.size.toString()
    } else {
        ""
    }
}
