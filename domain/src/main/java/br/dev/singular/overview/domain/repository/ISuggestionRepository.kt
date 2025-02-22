package br.dev.singular.overview.domain.repository

import br.dev.singular.overview.domain.model.Suggestion


interface ISuggestionRepository {

    suspend fun getAll(): List<Suggestion>

}
