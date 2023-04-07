package br.com.deepbyte.overview.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import br.com.deepbyte.overview.data.source.media.MediaTypeEnum as MediaType

@Parcelize
class Filters(
    var mediaType: MediaType = MediaType.ALL,
    private val genresIds: MutableList<Long> = mutableListOf()
) : Parcelable {

    fun hasGenreIds(genreId: Long) = genresIds.any { it == genreId }

    fun updateGenreIds(genreId: Long) = if (hasGenreIds(genreId)) {
        genresIds.remove(genreId)
    } else {
        genresIds.add(genreId)
    }

    fun cleanGenreIds() = genresIds.clear()

    fun genresIsEmpty() = genresIds.isEmpty()
}
