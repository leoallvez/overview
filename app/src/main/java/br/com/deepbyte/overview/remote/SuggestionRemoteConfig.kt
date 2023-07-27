package br.com.deepbyte.overview.remote

import br.com.deepbyte.overview.data.model.Suggestion
import br.com.deepbyte.overview.util.IJsonFileReader
import br.com.deepbyte.overview.util.parseToList
import io.github.leoallvez.firebase.RemoteConfigKey.SUGGESTIONS_LIST_KEY
import io.github.leoallvez.firebase.RemoteSource

class SuggestionRemoteConfig(
    private val _jsonFileReader: IJsonFileReader,
    private val _remoteSource: RemoteSource
) : RemoteConfig<List<Suggestion>> {

    override fun execute() = getRemoteSuggestions()
        .ifEmpty { getLocalSuggestions() }

    private fun getLocalSuggestions(): List<Suggestion> {
        val json = _jsonFileReader.read(SUGGESTIONS_FILE_NAME)
        return json.toSuggestionList()
    }

    private fun getRemoteSuggestions(): List<Suggestion> {
        val json = _remoteSource.getString(SUGGESTIONS_LIST_KEY)
        return json.toSuggestionList()
    }

    private fun String.toSuggestionList() = parseToList<Suggestion>()

    companion object {
        private const val SUGGESTIONS_FILE_NAME = "suggestions.json"
    }
}
