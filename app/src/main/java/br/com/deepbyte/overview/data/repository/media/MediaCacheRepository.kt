package br.com.deepbyte.overview.data.repository.media

import br.com.deepbyte.overview.data.model.media.Media
import br.com.deepbyte.overview.data.model.media.MediaEntity
import br.com.deepbyte.overview.data.model.media.Movie
import br.com.deepbyte.overview.data.model.media.TvShow
import br.com.deepbyte.overview.data.repository.media.interfaces.IMediaCacheRepository
import br.com.deepbyte.overview.data.source.media.local.suggestion.MediaLocalDataSource
import br.com.deepbyte.overview.data.source.media.remote.IMediaRemoteDataSource
import br.com.deepbyte.overview.data.source.streaming.StreamingLocalDataSource
import timber.log.Timber
import javax.inject.Inject

class MediaCacheRepository @Inject constructor(
    private val _sourceLocal: MediaLocalDataSource,
    private val _movieSource: IMediaRemoteDataSource<Movie>,
    private val _tvShowSource: IMediaRemoteDataSource<TvShow>,
    private val _streamingLocalDataSource: StreamingLocalDataSource
) : IMediaCacheRepository {

    private val selectedStreamingIds: List<Long> by lazy {
        _streamingLocalDataSource.getAllSelected().map { it.apiId }
    }

    override suspend fun saveCache(): Boolean = with(_sourceLocal) {
        val newMedias = getMedias()
        Timber.i("New medias: ${newMedias.size}")
        return@with if (newMedias.isNotEmpty()) {
            deleteNotLiked()
            updateAttributes(newMedias)
            insert(newMedias)
            true
        } else {
            false
        }
    }

    private suspend fun getMedias(): List<MediaEntity> {
        val movies = _movieSource.discoverByStreamings(selectedStreamingIds)
        val tvShows = _tvShowSource.discoverByStreamings(selectedStreamingIds)
        return filterMedias(medias = tvShows + movies)
    }

    private fun filterMedias(medias: List<Media>): List<MediaEntity> {
        return medias
            .asSequence()
            .filter { it.backdropPath.isNullOrEmpty().not() && it.adult.not() }
            .distinctBy { it.apiId }
            .sortedByDescending { it.voteAverage }
            .map { it.toMediaEntity() }
            .take(n = 5)
            .toList()
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
