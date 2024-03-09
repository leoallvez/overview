package br.dev.singular.overview.data.model.filters

import android.os.Parcelable
import br.dev.singular.overview.data.model.provider.StreamingEntity
import br.dev.singular.overview.data.source.media.MediaType
import br.dev.singular.overview.util.isNull
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchFilters(
    val query: String = "",
    val genreId: Long? = null,
    val streaming: StreamingEntity? = null,
    val mediaType: MediaType = MediaType.ALL
) : Parcelable {

    fun areDefaultValues() = query.isEmpty() && mediaType.isDefault() && genreId.isNull()
}
