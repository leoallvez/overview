package br.dev.singular.overview.data.repository.media

import br.dev.singular.overview.data.local.source.IMediaLocalDataSource
import br.dev.singular.overview.data.util.mappers.dataToDomain.toDomain
import br.dev.singular.overview.data.util.mappers.domainToData.toData
import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.repository.GetAll
import br.dev.singular.overview.domain.repository.Update
import javax.inject.Inject

class MediaFavoriteLocalRepository @Inject constructor(
    private val dataSource: IMediaLocalDataSource
) : GetAll<Media>, Update<List<Media>> {

    override suspend fun getAll() =
        dataSource.getAll().filter { it.isLiked }.map { it.toDomain() }

    // Inserted instead of updated to keep the last update date that defines the favorites order.
    override suspend fun update(item: List<Media>) {
        dataSource.insert(item.map { it.toData() })
    }
}
