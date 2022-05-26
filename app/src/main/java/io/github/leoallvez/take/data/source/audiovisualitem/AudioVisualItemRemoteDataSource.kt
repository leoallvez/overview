package io.github.leoallvez.take.data.source.audiovisualitem

import com.haroldadmin.cnradapter.NetworkResponse
import io.github.leoallvez.take.data.api.ApiService
import io.github.leoallvez.take.data.source.AudiovisualResult
import io.github.leoallvez.take.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AudioVisualItemRemoteDataSource @Inject constructor(
    private val api: ApiService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun get(url: String): AudiovisualResult {
        return withContext(ioDispatcher) {
            when (val response = api.getAudioVisualItems(url)) {
                is NetworkResponse.Success -> AudiovisualResult.ApiSuccess(
                    content = response.body.results
                )
                is NetworkResponse.ServerError -> AudiovisualResult.ApiError(
                    code = response.code,
                    message = response.body?.statusMessage
                )
                is NetworkResponse.NetworkError -> AudiovisualResult.ApiNetworkError
                is NetworkResponse.UnknownError -> AudiovisualResult.ApiUnknownError
            }
        }
    }
}
