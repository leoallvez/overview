package br.dev.singular.overview.data.source.video

import br.dev.singular.overview.data.model.media.Video

interface IVideoRemoteDataSource {
    suspend fun getItems(apiId: Long, type: String): List<Video>
}
