package br.com.deepbyte.overview.data.repository.media.interfaces

interface IMediaCacheRepository {
    suspend fun saveCache(): Boolean
}
