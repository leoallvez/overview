package br.dev.singular.overview.data.model.filters

import android.os.Parcelable
import br.dev.singular.overview.util.joinToStringWithComma
import kotlinx.parcelize.Parcelize
import br.dev.singular.overview.data.source.media.MediaTypeEnum as MediaType

@Parcelize
class SearchFilters(
    var query: String = "",
    var mediaType: MediaType = MediaType.ALL,
    var streamingId: Long? = null,
    // TODO: remover a lógica de multipla seleção de gêneros;
    val genresIds: MutableList<Long> = mutableListOf()
) : Parcelable {

    fun hasGenreWithId(genreId: Long) = genresIds.any { it == genreId }

    fun updateGenreIds(genreId: Long) {
        val hasGenre = hasGenreWithId(genreId)
        if (hasGenre) {
            genresIds.remove(genreId)
        } else {
            genresIds.add(genreId)
        }
        genresIds.removeAll { it != genreId }
    }

    fun getGenreIdsSeparatedWithComma() = genresIds.joinToStringWithComma()

    fun genresIsIsNotEmpty() = genresIds.isNotEmpty()
    fun clearGenresIds() = genresIds.clear()

    fun clear() {
        mediaType = MediaType.ALL
        clearGenresIds()
    }

    fun areDefaultValues() =
        query.isEmpty() && mediaType == MediaType.ALL && genresIds.isEmpty()

    fun genreQuantity(): String {
        val mediaTypeQuantity = if (mediaType != MediaType.ALL) 1 else 0
        val filtersQuantity = genresIds.size + mediaTypeQuantity
        return if (filtersQuantity > 0) filtersQuantity.toString() else String()
    }
}
