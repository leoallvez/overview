package br.dev.singular.overview.data.source.media.remote

import br.dev.singular.overview.data.model.media.Media

interface IMediaDiscoverRemoteDataSource<T : Media> {
    suspend fun discoverByStreaming(streamingIds: List<Long>): List<T>
}
