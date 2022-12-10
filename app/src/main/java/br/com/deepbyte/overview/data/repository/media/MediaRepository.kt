package br.com.deepbyte.overview.data.repository.media

import br.com.deepbyte.overview.data.api.response.MediaDetailResponse
import br.com.deepbyte.overview.data.api.response.Provider
import br.com.deepbyte.overview.data.api.response.ProviderPlace
import br.com.deepbyte.overview.data.source.DataResult
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

    override suspend fun getItem(apiId: Long, mediaType: String) = withContext(_dispatcher) {
        val result = _mediaDataSource.getItem(apiId, mediaType)
        if (result is DataResult.Success) {
            setSimilarType(result.data, mediaType)
            result.data?.providers = getProviders(apiId, mediaType)
        }
        flow { emit(result) }
    }

    private suspend fun getProviders(apiId: Long, mediaType: String): List<ProviderPlace> {
        val result = _providerDataSource.getItems(apiId, mediaType)
        val resultsMap = result.data?.results ?: mapOf<String, Provider>()
        val entries = resultsMap.filter { it.key == "BR" }.entries
        return if (entries.isNotEmpty()) {
            entries.first().value.getOrderedFlatRate()
        } else {
            listOf()
        }
    }

    private fun setSimilarType(mediaDetails: MediaDetailResponse?, mediaType: String) {
        mediaDetails?.similar?.results?.forEach { it.type = mediaType }
    }
}
