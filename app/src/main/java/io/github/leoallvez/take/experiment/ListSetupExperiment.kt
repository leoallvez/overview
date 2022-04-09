package io.github.leoallvez.take.experiment

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.leoallvez.firebase.RemoteConfigKey.LIST_SETUP_KEY
import io.github.leoallvez.firebase.RemoteSource
import io.github.leoallvez.take.data.model.ListSetup
import io.github.leoallvez.take.util.IJsonFileReader
import java.io.IOException
import javax.inject.Inject

class ListSetupExperiment @Inject constructor(
    private val jsonFileReader: IJsonFileReader,
    private val remoteSource: RemoteSource
) : AbExperiment<List<ListSetup>> {

    //TODO: refactor all class
    var json: String? = ""
        private set

    override fun execute(): List<ListSetup> {
        return getLocalListsSetup() ?: getRemoteListsSetup()
    }

    private fun getLocalListsSetup(): List<ListSetup>? {
        json = jsonFileReader.read(LISTS_SETUP_FILE)
        return parseJson()
    }

    private fun getRemoteListsSetup(): List<ListSetup> {
        json = remoteSource.getString(LIST_SETUP_KEY)
        return parseJson() ?: listOf()
    }

    // Work with generics???
    private fun parseJson() : List<ListSetup>? {
        return try {
            val gson = Gson()
            val listPersonType = object : TypeToken<List<ListSetup>>() {}.type
            gson.fromJson(json, listPersonType)
        } catch (io: IOException) {
            null
        }
    }

    companion object {
        const val LISTS_SETUP_FILE = "lists_setup.json"
    }
}
