package br.dev.singular.overview.data.source.media.remote

import br.dev.singular.overview.data.model.media.Media

interface IMediaDiscoverRemoteDataSource<T : Media> {
    suspend fun discoverByStreamings(streamingsIds: List<Long>): List<T>
}
