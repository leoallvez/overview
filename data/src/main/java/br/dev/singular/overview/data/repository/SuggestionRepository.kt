package br.dev.singular.overview.data.repository

import br.dev.singular.overview.domain.model.Suggestion
import br.dev.singular.overview.domain.repository.Create
import br.dev.singular.overview.domain.repository.Delete
import br.dev.singular.overview.domain.repository.GetAll

class SuggestionRepository: Create<Suggestion>, GetAll<Suggestion>, Delete<Suggestion> {

    override suspend fun create(vararg items: Suggestion) {
        TODO("Not yet implemented")
    }

    override suspend fun getAll(): List<Suggestion> {
        TODO("Not yet implemented")
    }

    override suspend fun delete(vararg items: Suggestion) {
        TODO("Not yet implemented")
    }
}
