package br.com.deepbyte.overview.data.repository.media

import br.com.deepbyte.overview.data.model.media.Media
import br.com.deepbyte.overview.data.model.media.MediaEntity
import br.com.deepbyte.overview.data.model.media.Movie
import br.com.deepbyte.overview.data.model.media.TvShow
import br.com.deepbyte.overview.data.repository.media.interfaces.IMediaCacheRepository
import br.com.deepbyte.overview.data.source.media.local.suggestion.MediaLocalDataSource
import br.com.deepbyte.overview.data.source.media.remote.IMediaRemoteDataSource
import br.com.deepbyte.overview.data.source.streaming.StreamingLocalDataSource
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class MediaCacheRepository @Inject constructor(
    private val _mediaLocalSource: MediaLocalDataSource,
    private val _streamingLocalSource: StreamingLocalDataSource,
    private val _movieRemoteSource: IMediaRemoteDataSource<Movie>,
    private val _tvShowRemoteSource: IMediaRemoteDataSource<TvShow>
) : IMediaCacheRepository {

    private val selectedStreamingIds: List<Long> by lazy {
        _streamingLocalSource.getAllSelected().map { it.apiId }
    }

    override suspend fun saveMediaCache(): Boolean = with(_mediaLocalSource) {
        val newMedias = getRemoteMedias()
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

    override suspend fun getMediaCache(): Flow<List<MediaEntity>> =
        _mediaLocalSource.getIndicated()

    private suspend fun getRemoteMedias(): List<MediaEntity> {
        val movies = _movieRemoteSource.discoverByStreamings(selectedStreamingIds)
        val tvShows = _tvShowRemoteSource.discoverByStreamings(selectedStreamingIds)
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
