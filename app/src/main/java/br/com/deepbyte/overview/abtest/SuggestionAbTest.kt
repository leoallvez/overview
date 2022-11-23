package br.com.deepbyte.overview.abtest

import br.com.deepbyte.overview.data.model.Suggestion
import br.com.deepbyte.overview.util.IJsonFileReader
import br.com.deepbyte.overview.util.parseToList
import io.github.leoallvez.firebase.RemoteConfigKey.SUGGESTIONS_LIST_KEY
import io.github.leoallvez.firebase.RemoteSource

import javax.inject.Inject

class SuggestionAbTest @Inject constructor(
    private val _jsonFileReader: IJsonFileReader,
    private val _remoteSource: RemoteSource
) : AbTest<List<Suggestion>> {

    override fun execute(): List<Suggestion> {
        val list = getRemoteSuggestions()
        return list.ifEmpty { getLocalSuggestions() }
    }

    private fun getLocalSuggestions(): List<Suggestion> {
        val json = _jsonFileReader.read(SUGGESTIONS_FILE_NAME)
        return parseJsonToSuggestions(json)
    }

    private fun getRemoteSuggestions(): List<Suggestion> {
        val json = _remoteSource.getString(SUGGESTIONS_LIST_KEY)
        return parseJsonToSuggestions(json)
    }

    private fun parseJsonToSuggestions(json: String): List<Suggestion> {
        return json.parseToList()
    }

    companion object {
        private const val SUGGESTIONS_FILE_NAME = "suggestions.json"
    }
}
