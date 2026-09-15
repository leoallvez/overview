package br.dev.singular.overview.domain.usecase.media

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.MediaKey
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.repository.GetByParam
import br.dev.singular.overview.domain.repository.Update

interface IMediaPersistenceUseCase {
    suspend fun getById(id: Long, type: MediaType): Media?
    suspend fun save(media: Media)
}

class MediaPersistenceUseCase(
    private val getter: GetByParam<Media?, MediaKey>,
    private val updater: Update<Media>
) : IMediaPersistenceUseCase {

    override suspend fun getById(id: Long, type: MediaType): Media? {
        return getter.getByParam(MediaKey(id, type))
    }

    override suspend fun save(media: Media) = updater.update(media)
}
