package br.com.deepbyte.overview.data.source.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import br.com.deepbyte.overview.data.model.provider.Streaming
import br.com.deepbyte.overview.data.source.streaming.IStreamingRemoteDataSource
import br.com.deepbyte.overview.data.source.streaming.StreamingLocalDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class StreamingSelectedUpdateWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val _sourceLocal: StreamingLocalDataSource,
    private val _sourceRemote: IStreamingRemoteDataSource
) : CoroutineWorker(context, params) {

    private val locals: List<Streaming> by lazy { _sourceLocal.getItems() }

    override suspend fun doWork(): Result {
        if (locals.isNotEmpty()) {
            val toUpdate = filterSalved().mapNotNull { transform(it) }
            _sourceLocal.update(*toUpdate.toTypedArray())
        }
        Timber.tag("work_manager").i(message = "StreamingSelectedUpdateWorker done")
        return Result.success()
    }

    private fun transform(stream: Streaming): Streaming? {
        val result = locals.find { it.apiId == stream.apiId }
        return result?.copy(name = stream.name, logoPath = stream.logoPath)
    }

    private suspend fun filterSalved() = locals.flatMap { local ->
        val remotes = _sourceRemote.getItems()
        remotes.filter { remote -> local.apiId == remote.apiId }
    }
}
