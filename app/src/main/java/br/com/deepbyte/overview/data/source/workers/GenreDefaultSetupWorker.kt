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

// https://developer.android.com/training/data-storage/room/relationships?hl=pt-br
@HiltWorker
class GenreDefaultSetupWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val _jsonFileReader: IJsonFileReader,
    private val _genreRepository: IGenreRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val genreTypeIsEmpty = _genreRepository.localGenreTypeIsEmpty()

        if (genreTypeIsEmpty) {
            val genreType = getGenreTypeInDefault()
            _genreRepository.insertGenreType(*genreType.toTypedArray())
        }

        _genreRepository.cacheGenre(MediaType.TV_SHOW)

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

/**
  1 - Verifica se o genero não existe na base.
    a) Se não existe, salva o genero e relação.
    b) Se sim salva apenas a relação

 */
