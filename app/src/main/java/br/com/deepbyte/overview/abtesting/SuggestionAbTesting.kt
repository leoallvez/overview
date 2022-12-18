package br.com.deepbyte.overview.abtesting

import br.com.deepbyte.overview.data.model.Suggestion
import br.com.deepbyte.overview.util.IJsonFileReader
import br.com.deepbyte.overview.util.parseToList
import io.github.leoallvez.firebase.RemoteConfigKey.SUGGESTIONS_LIST_KEY
import io.github.leoallvez.firebase.RemoteSource

class SuggestionAbTesting(
    private val _jsonFileReader: IJsonFileReader,
    private val _remoteSource: RemoteSource
) : AbTesting<List<Suggestion>> {

    override fun execute(): List<Suggestion> {
        val suggestions = getRemoteSuggestions()
        return suggestions.ifEmpty { getLocalSuggestions() }
    }

    private fun getLocalSuggestions(): List<Suggestion> {
        val json = _jsonFileReader.read(SUGGESTIONS_FILE_NAME)
        return parseJsonToSuggestions(json)
    }

    private fun getRemoteSuggestions(): List<Suggestion> {
        val json = _remoteSource.getString(SUGGESTIONS_LIST_KEY)
        return parseJsonToSuggestions(json)
    }

    private fun parseJsonToSuggestions(json: String) =
        json.parseToList<Suggestion>()

    companion object {
        private const val SUGGESTIONS_FILE_NAME = "suggestions.json"
    }
}
