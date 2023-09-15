package br.dev.singular.overview.data.source.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import br.dev.singular.overview.data.model.media.MediaTypeEntity
import br.dev.singular.overview.data.repository.genre.IGenreRepository
import br.dev.singular.overview.data.repository.mediatype.IMediaTypeRepository
import br.dev.singular.overview.data.source.media.MediaTypeEnum
import br.dev.singular.overview.util.IJsonFileReader
import br.dev.singular.overview.util.parseToList
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class GenreDefaultSetupWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val _jsonFileReader: IJsonFileReader,
    private val _genreRepository: IGenreRepository,
    private val _mediaTypeRepository: IMediaTypeRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val mediaTypeNotCached = _mediaTypeRepository.notCached()
        if (mediaTypeNotCached) {
            cacheMediaType()
            cacheGenre()
        }
        return Result.success()
    }

    private suspend fun cacheMediaType() {
        val mediaType = getMediaTypeInDefault()
        _mediaTypeRepository.insert(mediaType)
    }

    private suspend fun cacheGenre() {
        _genreRepository.cacheWithType(MediaTypeEnum.TV_SHOW)
        _genreRepository.cacheWithType(MediaTypeEnum.MOVIE)
    }

    private fun getMediaTypeInDefault(): List<MediaTypeEntity> {
        val json = _jsonFileReader.read(GENRE_TYPE_FILE_NAME)
        return json.parseToList()
    }

    companion object {
        const val GENRE_TYPE_FILE_NAME = "genre_type.json"
    }
}
