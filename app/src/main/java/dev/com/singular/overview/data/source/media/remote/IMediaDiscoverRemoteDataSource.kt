package dev.com.singular.overview.data.source.media.remote

import dev.com.singular.overview.data.model.media.Media

interface IMediaDiscoverRemoteDataSource<T : Media> {
    suspend fun discoverByStreamings(streamingsIds: List<Long>): List<T>
}
