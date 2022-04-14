package io.github.leoallvez.take.data.api.repository.loading

import io.github.leoallvez.take.data.db.TakeDatabase
import io.github.leoallvez.take.data.db.dao.SuggestionsDao
import io.github.leoallvez.take.data.model.Suggestion
import io.github.leoallvez.take.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoadingDbDataSource @Inject constructor(
    private val database: TakeDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
){

    private val daoSuggestions: SuggestionsDao by lazy {
        database.suggestionDao()
    }

    suspend fun saveSuggestion(suggestion: Suggestion): Long {
        return withContext(ioDispatcher) {
            daoSuggestions.insert(suggestion)
        }
    }

    suspend fun refreshSuggestions(
        suggestions: List<Suggestion>
    ) {
        withContext (ioDispatcher) {
            if(suggestions.isNotEmpty()) {
                daoSuggestions.deleteAll()
                val toSave = suggestions.toTypedArray()
                daoSuggestions.insert(*toSave)
            }
        }
    }

}
