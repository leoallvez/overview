package br.com.deepbyte.overview.data.source.media.local.suggestion

import br.com.deepbyte.overview.data.db.dao.MediaSuggestionDao
import br.com.deepbyte.overview.data.model.media.MediaSuggestion
import javax.inject.Inject

class MediaSuggestionLocalDataSource @Inject constructor(
    private val _dao: MediaSuggestionDao
) {
    fun insert(vararg mediaSuggestion: MediaSuggestion) =
        _dao.insert(*mediaSuggestion)

    fun getAll() = _dao.getAll()

    fun deleteAll() = _dao.deleteAll()
}
