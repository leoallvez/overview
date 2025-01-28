package br.dev.singular.overview.data.source.trailer

import br.dev.singular.overview.data.model.media.Trailer

interface ITrailerRemoteDataSource {
    suspend fun getAll(type: String, apiId: Long): List<Trailer>
}
