package br.com.deepbyte.overview.data.repository.media

import br.com.deepbyte.overview.data.model.media.MediaEntity
import br.com.deepbyte.overview.data.repository.media.interfaces.IMediaCacheRepository
import br.com.deepbyte.overview.data.sampe.mediaEntitySamples
import br.com.deepbyte.overview.data.source.media.local.suggestion.MediaLocalDataSource
import javax.inject.Inject

class MediaCacheRepository @Inject constructor(
    private val _sourceLocal: MediaLocalDataSource
) : IMediaCacheRepository {

    override fun saveCache(): Boolean = with(_sourceLocal) {
        val newMedias = mediaEntitySamples
        return@with if (newMedias.isNotEmpty()) {
            deleteNotLiked()
            updateAttributes(newMedias)
            insert(newMedias)
            true
        } else {
            false
        }
    }

    private fun updateAttributes(newMedias: List<MediaEntity>) {
        val likedMedias = _sourceLocal.getLiked()
        newMedias.forEach { newMedia ->
            val likedMedia = likedMedias.find { it.apiId == newMedia.apiId }
            likedMedia?.let {
                newMedia.isLiked = true
                newMedia.dbId = it.dbId
            }
            newMedia.isIndicated = true
        }
    }
}
