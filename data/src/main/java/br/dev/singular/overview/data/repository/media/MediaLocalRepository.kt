package br.dev.singular.overview.data.repository.media

import br.dev.singular.overview.data.local.source.IMediaLocalDataSource
import br.dev.singular.overview.data.util.mappers.dataToDomain.toDomain
import br.dev.singular.overview.data.util.mappers.domainToData.toData
import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.QueryState
import br.dev.singular.overview.domain.repository.CrudRepository
import br.dev.singular.overview.domain.repository.GetPage
import javax.inject.Inject

class MediaLocalRepository @Inject constructor(
    private val dataSource: IMediaLocalDataSource
) : CrudRepository<Media>, GetPage<Media, QueryState> {

    override suspend fun getAll() = dataSource.getAll().map { it.toDomain() }

    override suspend fun getById(id: Long): Media? {
        return dataSource.getById(id)?.toDomain()
    }

    override suspend fun update(item: Media) {
        dataSource.update(item.toData())
    }

    override suspend fun getPage(param: QueryState) = with(param) {
        dataSource.getPage(page, isLiked, type.toData()).toDomain()
    }

    override suspend fun delete(vararg items: Media) {
        dataSource.delete(items.map { it.toData() })
    }
}
