package io.github.leoallvez.take.abtest

import io.github.leoallvez.firebase.RemoteConfigKey.LIST_SETUP_KEY
import io.github.leoallvez.firebase.RemoteSource
import io.github.leoallvez.take.data.model.Suggestion
import io.github.leoallvez.take.util.IJsonFileReader
import io.github.leoallvez.take.util.getList
import javax.inject.Inject

class SuggestionAbTest @Inject constructor(
    private val jsonFileReader: IJsonFileReader,
    private val remoteSource: RemoteSource
) : AbTest<List<Suggestion>> {

    override fun execute(): List<Suggestion> {
        return getLocalSuggestions() ?: getRemoteSuggestions()
    }

    private fun getLocalSuggestions(): List<Suggestion>? {
        val json = jsonFileReader.read(SUGGESTIONS_FILE)
        return parseJsonToSuggestions(json)
    }

    private fun getRemoteSuggestions(): List<Suggestion> {
        val json = remoteSource.getString(LIST_SETUP_KEY)
        return parseJsonToSuggestions(json) ?: listOf()
    }

    private fun parseJsonToSuggestions(json: String): List<Suggestion>? {
        return json.getList(Suggestion::class.java)
    }

    companion object {
        private const val SUGGESTIONS_FILE = "suggestions.json"
    }
}
