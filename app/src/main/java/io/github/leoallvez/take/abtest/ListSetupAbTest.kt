package io.github.leoallvez.take.abtest

import io.github.leoallvez.firebase.RemoteConfigKey.LIST_SETUP_KEY
import io.github.leoallvez.firebase.RemoteSource
import io.github.leoallvez.take.data.model.Suggestion
import io.github.leoallvez.take.util.IJsonFileReader
import io.github.leoallvez.take.util.getList
import javax.inject.Inject

class ListSetupAbTest @Inject constructor(
    private val jsonFileReader: IJsonFileReader,
    private val remoteSource: RemoteSource
) : AbTest<List<Suggestion>> {

    override fun execute(): List<Suggestion> {
        return getLocalListsSetup() ?: getRemoteListsSetup()
    }

    private fun getLocalListsSetup(): List<Suggestion>? {
        val json = jsonFileReader.read(LISTS_SETUP_FILE)
        return parseJsonToListSetup(json)
    }

    private fun getRemoteListsSetup(): List<Suggestion> {
        val json = remoteSource.getString(LIST_SETUP_KEY)
        return parseJsonToListSetup(json) ?: listOf()
    }

    private fun parseJsonToListSetup(json: String): List<Suggestion>? {
        return json.getList(Suggestion::class.java)
    }

    companion object {
        const val LISTS_SETUP_FILE = "lists_setup.json"
    }
}
