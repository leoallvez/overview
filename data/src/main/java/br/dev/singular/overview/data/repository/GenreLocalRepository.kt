package br.dev.singular.overview.data.repository

import br.dev.singular.overview.data.local.source.IGenreLocalDataSource
import br.dev.singular.overview.data.util.mappers.dataToDomain.toDomain
import br.dev.singular.overview.data.util.mappers.domainToData.toData
import br.dev.singular.overview.domain.model.Genre
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.MediaTypeGenres
import br.dev.singular.overview.domain.repository.GetByParam
import br.dev.singular.overview.domain.repository.Update
import javax.inject.Inject

class GenreLocalRepository @Inject constructor(
    private val dataSource: IGenreLocalDataSource
) : GetByParam<List<Genre>, MediaType>, Update<MediaTypeGenres> {

    override suspend fun getByParam(param: MediaType): List<Genre> {
        return dataSource.getByMediaType(type = param.toData()).map { it.toDomain() }
    }

    override suspend fun update(item: MediaTypeGenres) = with(dataSource) {
        insertGenres(models = item.genres.map { it.toData() })
        insertMediaTypeGenres(models = item.toData())
    }
}
