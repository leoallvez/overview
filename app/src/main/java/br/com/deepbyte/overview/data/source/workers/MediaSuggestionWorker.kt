package br.com.deepbyte.overview.data.source.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import br.com.deepbyte.overview.data.sampe.mediaSuggestionSample
import br.com.deepbyte.overview.data.source.media.local.suggestion.MediaSuggestionLocalDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class MediaSuggestionWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val _sourceLocal: MediaSuggestionLocalDataSource
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        _sourceLocal.deleteAll()
        _sourceLocal.insert(*mediaSuggestionSample.toTypedArray())
        return Result.success()
    }
}
