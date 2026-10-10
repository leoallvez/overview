package br.dev.singular.overview.data.repository.media

import br.dev.singular.overview.data.local.source.IGenreLocalDataSource
import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.network.source.DataResult
import br.dev.singular.overview.data.network.source.ICatalogRemoteDataSource
import br.dev.singular.overview.data.network.source.IVideoRemoteDataSource
import br.dev.singular.overview.data.util.mappers.dataToDomain.toDomain
import br.dev.singular.overview.domain.model.Catalog
import br.dev.singular.overview.domain.model.Genre
import br.dev.singular.overview.domain.model.Video
import kotlin.coroutines.cancellation.CancellationException

internal suspend fun ICatalogRemoteDataSource.fetchCatalogs(
    id: Long,
    type: MediaDataType
): List<Catalog> = getCatalogsByMedia(id, type).map { it.toDomain() }

internal suspend fun IVideoRemoteDataSource.fetchVideos(
    id: Long,
    type: MediaDataType
): List<Video> = when (val response = getVideos(id, type)) {
    is DataResult.Success -> response.data.results.map { it.toDomain() }
    is DataResult.Error -> emptyList()
}

internal suspend fun IGenreLocalDataSource.fillOriginalNames(genres: List<Genre>): List<Genre> {
    val originalNames = runCatching { getByIds(genres.map { it.id }) }
        .getOrElse {
            if (it is CancellationException) throw it
            emptyList()
        }
        .associate { it.id to it.originalName }
    return genres.map { it.copy(originalName = originalNames[it.id].orEmpty()) }
}
