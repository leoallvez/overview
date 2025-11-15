package br.dev.singular.overview.data.source.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import br.dev.singular.overview.data.api.ApiLocale
import br.dev.singular.overview.data.repository.streaming.StreamingRepository
import br.dev.singular.overview.di.StreamingList
import br.dev.singular.overview.remote.RemoteConfig
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import br.dev.singular.overview.data.model.provider.StreamingEntity as Streaming

@HiltWorker
class StreamingSaveWorker @AssistedInject constructor(
    locale: ApiLocale,
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val _repository: StreamingRepository,
    @param:StreamingList
    private val _remote: RemoteConfig<List<Streaming>>
) : CoroutineWorker(context, params) {

    private val _region: String = locale.region
    private val _remoteStreams: List<Streaming> by lazy { _remote.execute() }

    override suspend fun doWork() = if (_remoteStreams.isNotEmpty()) {
        saveData(_remoteStreams)
        Result.success()
    } else {
        val apiStreams = getApiStream()
        if (apiStreams.isNotEmpty()) {
            saveData(apiStreams)
            Result.success()
        } else {
            Result.failure()
        }
    }

    private suspend fun saveData(streaming: List<Streaming>) {
        _repository.updateAllLocal(streaming = streaming)
        saveSelectedStreamCache(allStreams = streaming)
    }

    private suspend fun saveSelectedStreamCache(allStreams: List<Streaming>) {
        getSelectedStreamCache { streamCache ->
            if (shouldSaveCache(streamCache, allStreams)) {
                val stream = allStreams.minByOrNull { it.priority }
                _repository.updateSelected(stream)
            }
        }
    }

    private fun shouldSaveCache(stream: Streaming?, streams: List<Streaming>): Boolean {
        return stream == null || streams.none { it.apiId == stream.apiId }
    }

    private suspend fun getSelectedStreamCache(callback: suspend (Streaming?) -> Unit) {
        _repository.getSelectedItem().collect { stream ->
            callback(stream)
        }
    }

    private suspend fun getApiStream() = _repository.getAllRemote(_region)
}
