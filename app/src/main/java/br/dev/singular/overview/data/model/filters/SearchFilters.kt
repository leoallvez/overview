package br.dev.singular.overview.data.model.filters

import android.os.Parcelable
import br.dev.singular.overview.data.source.media.MediaType
import br.dev.singular.overview.util.isNull
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchFilters(
    val query: String = "",
    val genreId: Long? = null,
    val streamingId: Long? = null,
    val mediaType: MediaType = MediaType.ALL
) : Parcelable {

    fun areDefaultValues(): Boolean {
        return query.isEmpty() && mediaType == MediaType.ALL && genreId.isNull()
    }
}
