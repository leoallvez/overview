package io.github.leoallvez.take.data.source.audiovisualitem

import com.haroldadmin.cnradapter.NetworkResponse
import io.github.leoallvez.take.data.api.ApiService
import io.github.leoallvez.take.data.source.AudioVisualResult
import io.github.leoallvez.take.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AudioVisualItemRemoteDataSource @Inject constructor(
    private val api: ApiService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun get(url: String): AudioVisualResult {
        return withContext(ioDispatcher) {
            when (val response = api.getAudioVisualItems(url)) {
                is NetworkResponse.Success -> AudioVisualResult.ApiSuccess(
                    items = response.body.results
                )
                is NetworkResponse.ServerError -> AudioVisualResult.ApiError(
                    code = response.code,
                    message = response.body?.statusMessage
                )
                is NetworkResponse.NetworkError -> AudioVisualResult.ApiNetworkError
                is NetworkResponse.UnknownError -> AudioVisualResult.ApiUnknownError
            }
        }
    }
}
