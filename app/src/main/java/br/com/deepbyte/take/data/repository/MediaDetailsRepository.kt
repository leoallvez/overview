package br.com.deepbyte.take.data.repository

import br.com.deepbyte.take.data.api.response.MediaDetailResponse
import br.com.deepbyte.take.data.api.response.ProviderPlace
import br.com.deepbyte.take.data.source.DataResult
import br.com.deepbyte.take.data.source.media_item.IMediaRemoteDataSource
import br.com.deepbyte.take.data.source.provider.IProviderRemoteDataSource
import br.com.deepbyte.take.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MediaDetailsRepository @Inject constructor(
    private val _mediaDataSource: IMediaRemoteDataSource,
    private val _providerDataSource: IProviderRemoteDataSource,
    @IoDispatcher private val _dispatcher: CoroutineDispatcher
) {

    suspend fun getMediaDetailsResult(apiId: Long, mediaType: String) = withContext(_dispatcher) {
        return@withContext flow {
            val result = _mediaDataSource.getMediaDetailsResult(apiId, mediaType)
            if (result is DataResult.Success) {
                setSimilarType(result.data, mediaType)
                result.data?.providers = getProviders(apiId, mediaType)
            }
            emit(result)
        }
    }

    private suspend fun getProviders(apiId: Long, mediaType: String): List<ProviderPlace> {
        val result = _providerDataSource.getProvidersResult(apiId, mediaType)
        val resultsMap = result.data?.results ?: mapOf()
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
