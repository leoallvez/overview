package br.dev.singular.overview.domain.repository

import br.dev.singular.overview.domain.model.Media

interface IMediaRepository {
    suspend fun getByPath(path: String): List<Media>
}
