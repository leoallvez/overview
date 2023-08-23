package br.com.deepbyte.overview.data.repository.media

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
            val likedMedias = getLiked()
            deleteNotLiked()

            newMedias.forEach { newMedia ->
                with(newMedia) {
                    val likedMedia = likedMedias.find { it.apiId == newMedia.apiId }
                    likedMedia?.let {
                        isLiked = true
                        dbId = likedMedia.dbId
                    }
                    isIndicated = true
                }
            }
            insert(newMedias)
            true
        } else {
            false
        }
    }
}
