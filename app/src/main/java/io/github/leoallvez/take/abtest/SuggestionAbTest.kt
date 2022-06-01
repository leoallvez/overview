package io.github.leoallvez.take.abtest

import io.github.leoallvez.firebase.RemoteConfigKey.LIST_SETUP_KEY
import io.github.leoallvez.firebase.RemoteSource
import io.github.leoallvez.take.data.model.Suggestion
import io.github.leoallvez.take.util.IJsonFileReader
import io.github.leoallvez.take.util.parseToList
import javax.inject.Inject

class SuggestionAbTest @Inject constructor(
    private val _jsonFileReader: IJsonFileReader,
    private val _remoteSource: RemoteSource
) : AbTest<List<Suggestion>> {

    override fun execute(): List<Suggestion> {
        return getRemoteSuggestions() ?: getLocalSuggestions()
    }

    private fun getLocalSuggestions(): List<Suggestion> {
        val json = _jsonFileReader.read(SUGGESTIONS_FILE)
        return parseJsonToSuggestions(json) ?: listOf()
    }

    private fun getRemoteSuggestions(): List<Suggestion>? {
        val json = _remoteSource.getString(LIST_SETUP_KEY)
        return parseJsonToSuggestions(json)
    }

    private fun parseJsonToSuggestions(json: String): List<Suggestion>? {
        return json.parseToList(Suggestion::class.java)
    }

    companion object {
        private const val SUGGESTIONS_FILE = "suggestions.json"
    }
}
