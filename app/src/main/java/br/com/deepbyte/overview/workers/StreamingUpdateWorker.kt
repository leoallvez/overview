package br.com.deepbyte.overview.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import br.com.deepbyte.overview.data.model.provider.Streaming
import br.com.deepbyte.overview.data.source.streaming.StreamingLocalDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class StreamingUpdateWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val _source: StreamingLocalDataSource
) : CoroutineWorker(context, params) {
    // TODO: get remote data
    private val remotes: List<Streaming> by lazy { listOf() }
    private val locals: List<Streaming> by lazy { _source.getAll() }

    override suspend fun doWork(): Result {
        if (locals.isNotEmpty()) {
            val diffs = filterSalved()
            val toUpdate = diffs.map { transform(it) }
            // TODO : update local data
            println(toUpdate)
        }
        return Result.success()
    }

    private fun transform(stream: Streaming): Streaming? {
        val result = locals.find { it.apiId == stream.apiId }
        return result?.copy(name = stream.name, logoPath = stream.logoPath)
    }

    private fun filterSalved() =
        locals.flatMap { l -> remotes.filter { r -> l.apiId == r.apiId } }
}
