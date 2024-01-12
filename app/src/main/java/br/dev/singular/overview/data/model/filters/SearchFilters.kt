package br.dev.singular.overview.data.model.filters

import android.os.Parcelable
import br.dev.singular.overview.data.source.media.MediaType
import br.dev.singular.overview.util.isNull
import kotlinx.parcelize.Parcelize

@Parcelize
class SearchFilters(
    var query: String = "",
    var genreId: Long? = null,
    var streamingId: Long? = null,
    var mediaType: MediaType = MediaType.ALL
) : Parcelable {

    fun clear() {
        mediaType = MediaType.ALL
        clearGenreId()
    }

    fun clearGenreId() {
        genreId = null
    }

    fun areDefaultValues(): Boolean {
        return query.isEmpty() && mediaType == MediaType.ALL && genreId.isNull()
    }
}
