package br.dev.singular.overview.data.repository.media.local.interfaces

import br.dev.singular.overview.data.model.media.MediaEntity
import java.util.Date

interface IMediaEntityRepository {

    suspend fun update(media: MediaEntity)

    suspend fun deleteUnlikedOlderThan(date: Date)
}
