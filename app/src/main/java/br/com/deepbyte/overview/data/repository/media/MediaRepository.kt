package br.com.deepbyte.overview.data.repository.media

import br.com.deepbyte.overview.data.MediaType
import br.com.deepbyte.overview.data.model.media.Media
import br.com.deepbyte.overview.data.model.provider.ProviderPlace
import br.com.deepbyte.overview.data.source.media.IMediaRemoteDataSource
import br.com.deepbyte.overview.data.source.provider.IProviderRemoteDataSource
import br.com.deepbyte.overview.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MediaRepository @Inject constructor(
    private val _mediaDataSource: IMediaRemoteDataSource,
    private val _providerDataSource: IProviderRemoteDataSource,
    @IoDispatcher private val _dispatcher: CoroutineDispatcher
) : IMediaRepository {

    override suspend fun getItem(apiId: Long, mediaType: String) = withContext(_dispatcher)  {
        val result = requestMedia(apiId, mediaType)
        result.data?.let {
            setSimilarType(it, mediaType)
            it.providers = getProviders(apiId, mediaType)
        }
        flow { emit(result) }
    }

    private suspend fun requestMedia(apiId: Long, type: String) =
        if (type == MediaType.MOVIE.key) {
            _mediaDataSource.getMovie(apiId)
        } else {
            _mediaDataSource.getTvShow(apiId)
        }

    private fun setSimilarType(media: Media, mediaType: String) {
        val similar = media.similar.results
        similar.forEach { it.type = mediaType }
    }

    private suspend fun getProviders(apiId: Long, mediaType: String): List<ProviderPlace> {
        val result = _providerDataSource.getItems(apiId, mediaType)
        val resultsMap = result.data?.results ?: mapOf()
        val entries = resultsMap.filter { it.key == "BR" }.entries
        return if (entries.isNotEmpty()) {
            entries.first().value.getOrderedFlatRate()
        } else {
            listOf()
        }
    }
}
