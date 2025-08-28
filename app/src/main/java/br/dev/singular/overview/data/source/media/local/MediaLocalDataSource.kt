package br.dev.singular.overview.data.source.media.local

import br.dev.singular.overview.data.local.database.dao.MediaDao
import br.dev.singular.overview.data.model.MediaDataModel
import javax.inject.Inject

class MediaLocalDataSource @Inject constructor(
    private val _dao: MediaDao
) {
    suspend fun insert(models: List<MediaDataModel>) = _dao.insert(*models.toTypedArray())

    suspend fun update(model: MediaDataModel) = _dao.update(model)

    suspend fun isLiked(id: Long) = _dao.getAll().any { it.id == id && it.isLiked }
}
