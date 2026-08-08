package br.dev.singular.overview.domain.usecase.media

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.repository.GetById
import br.dev.singular.overview.domain.repository.Update

interface IMediaPersistenceUseCase {
    suspend fun getById(id: Long): Media?
    suspend fun save(media: Media)
}

class MediaPersistenceUseCase(
    private val getter: GetById<Media>,
    private val updater: Update<Media>
) : IMediaPersistenceUseCase {

    override suspend fun getById(id: Long): Media? = getter.getById(id)

    override suspend fun save(media: Media) = updater.update(media)
}
