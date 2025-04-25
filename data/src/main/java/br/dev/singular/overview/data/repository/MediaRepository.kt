package br.dev.singular.overview.data.repository

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.repository.IMediaRepository

class MediaRepository : IMediaRepository {
    
    override suspend fun getByPath(path: String): List<Media> {
        TODO("Not yet implemented")
    }
}
