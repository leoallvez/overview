package br.dev.singular.overview.data.local.source

import br.dev.singular.overview.data.local.database.dao.PendingFavoriteDao
import br.dev.singular.overview.data.model.MediaDataModel
import br.dev.singular.overview.data.model.PendingFavoriteDataModel
import javax.inject.Inject

interface IPendingFavoriteLocalDataSource {
    suspend fun getAll(): List<MediaDataModel>

    suspend fun clear()
}

class PendingFavoriteLocalDataSource @Inject constructor(
    private val dao: PendingFavoriteDao
) : IPendingFavoriteLocalDataSource {

    override suspend fun getAll(): List<MediaDataModel> = dao.getAll().map { it.toMedia() }

    override suspend fun clear() = dao.deleteAll()

    private fun PendingFavoriteDataModel.toMedia() = MediaDataModel(
        id = id,
        name = title,
        title = title,
        posterPath = posterPath,
        type = type,
        isLiked = true,
        lastUpdate = lastUpdate
    )
}
