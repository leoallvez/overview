package br.dev.singular.overview.data.repository.media

import br.dev.singular.overview.data.model.media.Media
import br.dev.singular.overview.data.model.media.MediaEntity
import br.dev.singular.overview.data.model.media.TvShow
import br.dev.singular.overview.data.repository.media.interfaces.IMediaCacheRepository
import br.dev.singular.overview.data.source.media.local.suggestion.MediaLocalDataSource
import br.dev.singular.overview.data.source.media.remote.IMediaDiscoverRemoteDataSource
import br.dev.singular.overview.data.source.streaming.StreamingLocalDataSource
import javax.inject.Inject

class MediaCacheRepository @Inject constructor(
    private val _mediaLocalSource: MediaLocalDataSource,
    private val _streamingLocalSource: StreamingLocalDataSource,
    private val _tvShowDiscoverRemoteSource: IMediaDiscoverRemoteDataSource<TvShow>
) : IMediaCacheRepository {

    private val selectedStreamingIds: List<Long> by lazy {
        _streamingLocalSource.getAllSelected().map { it.apiId }
    }

    override suspend fun saveMediaCache(): Boolean = with(_mediaLocalSource) {
        val newMedias = getFilteredMedias()
        return@with if (newMedias.isNotEmpty()) {
            deleteNotLiked()
            updateAttributes(newMedias)
            insert(newMedias)
            true
        } else {
            false
        }
    }

    override suspend fun getMediaCache() = _mediaLocalSource.getIndicated()

    private suspend fun getFilteredMedias(): List<MediaEntity> {
        return getRemoteMedias()
            .asSequence()
            .filter { it.backdropPath.isNullOrEmpty().not() }
            .distinctBy { it.apiId }
            .map { it.toMediaEntity() }
            .distinctBy { it.apiId }
            .take(n = 5)
            .toList()
    }

    private suspend fun getRemoteMedias(): List<Media> {
        return _tvShowDiscoverRemoteSource.discoverByStreaming(selectedStreamingIds)
    }

    private fun updateAttributes(newMedias: List<MediaEntity>) {
        val likedMedias = _mediaLocalSource.getLiked()
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
