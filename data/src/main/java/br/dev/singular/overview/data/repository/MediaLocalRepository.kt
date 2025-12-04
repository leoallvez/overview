package br.dev.singular.overview.data.repository

import br.dev.singular.overview.data.local.source.IMediaLocalDataSource
import br.dev.singular.overview.data.util.mappers.dataToDomain.toDomain
import br.dev.singular.overview.data.util.mappers.domainToData.toData
import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.MediaParam
import br.dev.singular.overview.domain.repository.Delete
import br.dev.singular.overview.domain.repository.GetAll
import br.dev.singular.overview.domain.repository.GetPage
import javax.inject.Inject

class MediaLocalRepository @Inject constructor(
    private val dataSource: IMediaLocalDataSource
) : GetAll<Media>, GetPage<Media, MediaParam>, Delete<Media> {

    override suspend fun getAll() = dataSource.getAll().map { it.toDomain() }

    override suspend fun getPage(param: MediaParam) = with(param) {
        dataSource.getPage(page, isLiked, type.toData()).toDomain()
    }

    override suspend fun delete(vararg items: Media) {
        dataSource.delete(items.map { it.toData() })
    }
}
