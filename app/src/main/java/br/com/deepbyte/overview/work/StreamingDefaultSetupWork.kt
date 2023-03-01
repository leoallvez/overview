package br.com.deepbyte.overview.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import br.com.deepbyte.overview.abtesting.AbTesting
import br.com.deepbyte.overview.data.model.provider.Streaming
import br.com.deepbyte.overview.data.source.streaming.StreamingLocalDataSource
import br.com.deepbyte.overview.di.AbStreamings
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class StreamingDefaultSetupWork @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val _source: StreamingLocalDataSource,
    @AbStreamings experiment: AbTesting<List<Streaming>>
) : CoroutineWorker(context, params) {

    private val streamings: List<Streaming> by lazy { experiment.execute() }

    override suspend fun doWork() = if (streamings.isNotEmpty()) {
        if (_source.isEmpty()) {
            _source.insert(*streamings.toTypedArray())
        }
        Timber.i(message = "doWork is called - success")
        Result.success()
    } else {
        Timber.i(message = "doWork is called - failure")
        Result.failure()
    }
}
