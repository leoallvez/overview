package io.github.leoallvez.take.experiment

import io.github.leoallvez.firebase.RemoteConfigKey.LIST_SETUP_KEY
import io.github.leoallvez.firebase.RemoteSource
import io.github.leoallvez.take.data.model.ListSetup
import io.github.leoallvez.take.util.IJsonFileReader
import io.github.leoallvez.take.util.getList
import javax.inject.Inject

class ListSetupExperiment @Inject constructor(
    private val jsonFileReader: IJsonFileReader,
    private val remoteSource: RemoteSource
) : AbExperiment<List<ListSetup>> {

    override fun execute(): List<ListSetup> {
        return getLocalListsSetup() ?: getRemoteListsSetup()
    }

    private fun getLocalListsSetup(): List<ListSetup>? {
        val json = jsonFileReader.read(LISTS_SETUP_FILE)
        return parseJsonToListSetup(json)
    }

    private fun getRemoteListsSetup(): List<ListSetup> {
        val json = remoteSource.getString(LIST_SETUP_KEY)
        return parseJsonToListSetup(json) ?: listOf()
    }

    private fun parseJsonToListSetup(json: String): List<ListSetup>? {
        return json.getList(ListSetup::class.java)
    }

    companion object {
        const val LISTS_SETUP_FILE = "lists_setup.json"
    }
}
