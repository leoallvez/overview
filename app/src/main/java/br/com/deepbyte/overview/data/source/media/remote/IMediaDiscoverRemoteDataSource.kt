package br.com.deepbyte.overview.data.source.media.remote

import br.com.deepbyte.overview.data.model.media.Media

interface IMediaDiscoverRemoteDataSource<T : Media> {
    suspend fun discoverByStreamings(streamingsIds: List<Long>): List<T>
}
