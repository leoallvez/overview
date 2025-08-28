package br.dev.singular.overview.data.local.source

import br.dev.singular.overview.data.BuildConfig
import br.dev.singular.overview.data.local.database.dao.MediaDao
import br.dev.singular.overview.data.model.MediaDataPage
import br.dev.singular.overview.data.model.MediaDataModel
import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.model.MediaDataType.ALL
import javax.inject.Inject

interface IMediaLocalDataSource {
    suspend fun insert(models: List<MediaDataModel>)

    suspend fun delete(models: List<MediaDataModel>)

    suspend fun getAll(): List<MediaDataModel>

    suspend fun getPage(
        page: Int = 1,
        isLiked: Boolean? = null,
        type: MediaDataType = ALL
    ): MediaDataPage

}

class MediaLocalDataSource @Inject constructor (
    private val dao: MediaDao
) : IMediaLocalDataSource {

    override suspend fun insert(models: List<MediaDataModel>) =
        dao.insert(*models.toTypedArray())

    override suspend fun delete(models: List<MediaDataModel>) =
        dao.delete(*models.toTypedArray())

    override suspend fun getAll() = dao.getAll()

    override suspend fun getPage(
        page: Int,
        isLiked: Boolean?,
        type: MediaDataType
    ): MediaDataPage {

        val pageSize = BuildConfig.PAGE_SIZE

        val offset = (page - 1) * pageSize

        val items = dao.getPage(
            type = type,
            limit = pageSize,
            isLiked = isLiked,
            offset = offset
        )

        return MediaDataPage(
            items = items,
            page = page,
            isLastPage = items.size < pageSize
        )
    }
}
