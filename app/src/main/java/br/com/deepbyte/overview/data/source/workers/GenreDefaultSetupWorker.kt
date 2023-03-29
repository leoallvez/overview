package br.com.deepbyte.overview.data.source.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import br.com.deepbyte.overview.data.MediaType
import br.com.deepbyte.overview.data.model.media.GenreType
import br.com.deepbyte.overview.data.repository.genre.IGenreRepository
import br.com.deepbyte.overview.util.IJsonFileReader
import br.com.deepbyte.overview.util.parseToList
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class GenreDefaultSetupWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val _jsonFileReader: IJsonFileReader,
    private val _repository: IGenreRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val genreTypeIsEmpty = _repository.localGenreTypeIsEmpty()

        if (genreTypeIsEmpty) {
            val genreType = getGenreTypeInDefault()
            _repository.insertGenreType(genreType)
        }
        _repository.cacheGenre(MediaType.TV_SHOW)
        _repository.cacheGenre(MediaType.MOVIE)

        return Result.success()
    }

    private fun getGenreTypeInDefault(): List<GenreType> {
        val json = _jsonFileReader.read(GENRE_TYPE_FILE_NAME)
        return json.parseToList()
    }

    companion object {
        const val GENRE_TYPE_FILE_NAME = "genre_type.json"
    }
}
