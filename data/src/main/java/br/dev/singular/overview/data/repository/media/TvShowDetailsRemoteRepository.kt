package br.dev.singular.overview.data.repository.media

import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.network.source.DataResult
import br.dev.singular.overview.data.network.source.ICatalogRemoteDataSource
import br.dev.singular.overview.data.network.source.IMediaRemoteDataSource
import br.dev.singular.overview.data.network.source.IVideoRemoteDataSource
import br.dev.singular.overview.data.util.mappers.dataToDomain.toDomain
import br.dev.singular.overview.domain.model.Catalog
import br.dev.singular.overview.domain.model.TvShowDetails
import br.dev.singular.overview.domain.model.Video
import br.dev.singular.overview.domain.repository.GetById
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class TvShowDetailsRemoteRepository @Inject constructor(
    private val mediaDataSource: IMediaRemoteDataSource,
    private val videoDataSource: IVideoRemoteDataSource,
    private val catalogDataSource: ICatalogRemoteDataSource,
) : GetById<TvShowDetails> {

    override suspend fun getById(id: Long): TvShowDetails? {
        val tvShow = when (val response = mediaDataSource.getTvShowById(id)) {
            is DataResult.Success -> response.data.toDomain()
            is DataResult.Error -> return null
        }

        return coroutineScope {
            val videosDeferred = async { getVideos(id) }
            val catalogsDeferred = async { getCatalogs(id) }

            tvShow.copy(
                videos = videosDeferred.await(),
                catalogs = catalogsDeferred.await(),
            )
        }
    }

    private suspend fun getCatalogs(id: Long): List<Catalog> =
        catalogDataSource.getCatalogsByMedia(id, MediaDataType.TV)
            .map { it.toDomain() }

    private suspend fun getVideos(id: Long): List<Video> =
        when (val response = videoDataSource.getVideos(id, MediaDataType.TV)) {
            is DataResult.Success -> response.data.results.map { it.toDomain() }
            is DataResult.Error -> emptyList()
        }
}
