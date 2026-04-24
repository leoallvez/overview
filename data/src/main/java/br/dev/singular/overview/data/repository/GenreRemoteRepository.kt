package br.dev.singular.overview.data.repository

import br.dev.singular.overview.data.network.source.IGenreRemoteDataSource
import br.dev.singular.overview.data.util.mappers.dataToDomain.toDomain
import br.dev.singular.overview.data.util.mappers.domainToData.toData
import br.dev.singular.overview.domain.model.Genre
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.repository.GetByParam
import javax.inject.Inject

class GenreRemoteRepository @Inject constructor(
    private val dataSource: IGenreRemoteDataSource
) : GetByParam<List<Genre>, MediaType> {

    override suspend fun getByParam(param: MediaType): List<Genre> {
        return dataSource.getByMediaType(type = param.toData()).map { it.toDomain() }
    }
}
