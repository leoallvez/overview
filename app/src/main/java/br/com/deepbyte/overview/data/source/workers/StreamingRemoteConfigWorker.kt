package br.com.deepbyte.overview.data.source.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import br.com.deepbyte.overview.abtesting.RemoteConfig
import br.com.deepbyte.overview.data.api.ApiLocale
import br.com.deepbyte.overview.data.model.provider.StreamingConfig
import br.com.deepbyte.overview.data.source.streaming.StreamingLocalDataSource
import br.com.deepbyte.overview.di.StreamingsRemote
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class StreamingRemoteConfigWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val _locale: ApiLocale,
    private val _source: StreamingLocalDataSource,
    @StreamingsRemote val remoteConfig: RemoteConfig<StreamingConfig>
) : CoroutineWorker(context, params) {

    private val streamingConfig: StreamingConfig by lazy { remoteConfig.execute() }

    override suspend fun doWork(): Result {
        return if (streamingConfig.active) {
            val streamingsRemote = getStreamingsRemoteByRegion()

            if (streamingsRemote.isNotEmpty()) {
                // _source.deleteAll()

                // TODO: replace data
                _source.insert(*streamingsRemote.toTypedArray())
                Result.success()
            } else {
                Result.failure()
            }
        } else {
            Result.failure()
        }
    }

    private fun getStreamingsRemoteByRegion() = streamingConfig
        .streamingsByRegion
        .filter { it.key == _locale.region }
        .values
        .flatten()
}
