package io.github.leoallvez.take.data.repository

import io.github.leoallvez.take.data.api.response.MediaDetailResponse
import io.github.leoallvez.take.data.api.response.ProviderPlace
import io.github.leoallvez.take.data.source.DataResult
import io.github.leoallvez.take.data.source.media_item.IMediaRemoteDataSource
import io.github.leoallvez.take.data.source.provider.IProviderRemoteDataSource
import io.github.leoallvez.take.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MediaDetailsRepository @Inject constructor(
    private val _mediaDataSource: IMediaRemoteDataSource,
    private val _providerDataSource: IProviderRemoteDataSource,
    @IoDispatcher private val _dispatcher: CoroutineDispatcher,
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
        return if(entries.isNotEmpty()) {
            entries.first().value.getOrderedFlatRate()
        } else {
            listOf()
        }
    }

    private fun setSimilarType(mediaDetails: MediaDetailResponse?, mediaType: String) {
        mediaDetails?.similar?.results?.forEach { it.type = mediaType  }
    }
}
