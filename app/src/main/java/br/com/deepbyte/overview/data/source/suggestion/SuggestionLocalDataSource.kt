package br.com.deepbyte.overview.data.source.suggestion

import br.com.deepbyte.overview.data.db.dao.SuggestionDao
import br.com.deepbyte.overview.data.model.MediaItem
import br.com.deepbyte.overview.data.model.Suggestion
import javax.inject.Inject

class SuggestionLocalDataSource @Inject constructor(
    private val _dao: SuggestionDao
) {

    fun getAll(): List<Suggestion> = _dao.getAll()

    suspend fun update(vararg entities: Suggestion) = _dao.update(*entities)

    fun getWithMediaItems(): Map<Suggestion, List<MediaItem>> {
        return _dao.getAllWithMediaItems()
    }
}
