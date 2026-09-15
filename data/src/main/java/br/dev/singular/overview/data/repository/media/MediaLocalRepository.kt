package br.dev.singular.overview.data.repository.media

import br.dev.singular.overview.data.local.source.IMediaLocalDataSource
import br.dev.singular.overview.data.util.mappers.dataToDomain.toDomain
import br.dev.singular.overview.data.util.mappers.domainToData.toData
import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.MediaKey
import br.dev.singular.overview.domain.model.QueryState
import br.dev.singular.overview.domain.repository.Delete
import br.dev.singular.overview.domain.repository.GetAll
import br.dev.singular.overview.domain.repository.GetByParam
import br.dev.singular.overview.domain.repository.GetPage
import br.dev.singular.overview.domain.repository.Update
import javax.inject.Inject

class MediaLocalRepository @Inject constructor(
    private val dataSource: IMediaLocalDataSource
) : GetAll<Media>, GetByParam<Media?, MediaKey>, Update<Media>, Delete<Media>,
    GetPage<Media, QueryState> {

    override suspend fun getAll() = dataSource.getAll().map { it.toDomain() }

    override suspend fun getByParam(param: MediaKey): Media? {
        return dataSource.getById(param.id, param.type.toData())?.toDomain()
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
