package br.com.deepbyte.overview.data.source.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import br.com.deepbyte.overview.data.MediaType
import br.com.deepbyte.overview.data.api.response.GenreListResponse
import br.com.deepbyte.overview.data.model.media.Genre
import br.com.deepbyte.overview.data.source.DataResult
import br.com.deepbyte.overview.data.source.DataResult.Success
import br.com.deepbyte.overview.data.source.genre.GenreLocalDataSource
import br.com.deepbyte.overview.data.source.genre.IGenreRemoteDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class GenreDefaultSetupWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val _remoteSource: IGenreRemoteDataSource,
    private val _localSource: GenreLocalDataSource
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        requestGenre(MediaType.MOVIE).forEach {
            Timber.i("GENRE MOVIES: ${it.name}")
        }
        val tvShowsGenre = requestGenre(MediaType.TV_SHOW)
        _localSource.insert(*tvShowsGenre.toTypedArray())
        return Result.success()
    }

    private suspend fun requestGenre(type: MediaType): List<Genre> {
        val result = _remoteSource.getItemByMediaType(type)
        return getGenreList(result)
    }

    private fun getGenreList(
        result: DataResult<GenreListResponse>
    ) = if (result is Success) {
        result.data?.genres ?: listOf()
    } else {
        listOf()
    }
}
