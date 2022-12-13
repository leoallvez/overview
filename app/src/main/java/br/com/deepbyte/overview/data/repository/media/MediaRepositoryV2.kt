package br.com.deepbyte.overview.data.repository.media

import br.com.deepbyte.overview.data.MediaType
import br.com.deepbyte.overview.data.model.media.Movie
import br.com.deepbyte.overview.data.model.media.TvShow
import br.com.deepbyte.overview.data.source.media.IMediaRemoteDataSourceV2
import br.com.deepbyte.overview.data.source.provider.IProviderRemoteDataSource
import br.com.deepbyte.overview.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MediaRepositoryV2 @Inject constructor(
    private val _movieDataSource: IMediaRemoteDataSourceV2<Movie>,
    private val _tvShowDataSource: IMediaRemoteDataSourceV2<TvShow>,
    private val _providerDataSource: IProviderRemoteDataSource,
    @IoDispatcher private val _dispatcher: CoroutineDispatcher
) : IMediaRepository {

    override suspend fun getItem(
        apiId: Long,
        mediaType: String
    ) = withContext(_dispatcher) {
        val result = requestMedia(apiId, mediaType)
        result.data?.providers = getProviders(apiId, mediaType)
        flow { emit(result) }
    }

    private suspend fun requestMedia(apiId: Long, type: String) =
        if (type == MediaType.MOVIE.key) {
            _movieDataSource.find(apiId)
        } else {
            _tvShowDataSource.find(apiId)
        }

    private suspend fun getProviders(apiId: Long, mediaType: String) =
        _providerDataSource.getItems(apiId, mediaType)
}
