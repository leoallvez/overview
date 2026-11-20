package br.dev.singular.overview.data.local.source

import br.dev.singular.overview.data.local.database.dao.GenreDao
import br.dev.singular.overview.data.model.GenreDataModel
import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.model.MediaTypeGenreDataModel
import javax.inject.Inject

interface IGenreLocalDataSource {

    suspend fun insertGenres(models: List<GenreDataModel>)

    suspend fun insertMediaTypeGenres(models: List<MediaTypeGenreDataModel>)

    suspend fun getByMediaType(type: MediaDataType): List<GenreDataModel>
}

class GenreLocalDataSource @Inject constructor(
    private val dao: GenreDao
) : IGenreLocalDataSource {

    override suspend fun insertGenres(models: List<GenreDataModel>) =
        dao.insert(*models.toTypedArray())

    override suspend fun insertMediaTypeGenres(models: List<MediaTypeGenreDataModel>) {
        dao.insert(*models.toTypedArray())
    }

    override suspend fun getByMediaType(type: MediaDataType) =
        dao.getByMediaType(type)
}
