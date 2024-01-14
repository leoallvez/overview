package br.dev.singular.overview.data.repository.media.local.interfaces

import java.util.Date

interface IMediaEntityRepository {
    suspend fun deleteUnlikedOlderThan(date: Date)
}
