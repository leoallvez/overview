package br.dev.singular.overview.data.network.source

import br.dev.singular.overview.data.model.SuggestionDataModel
import br.dev.singular.overview.data.remote.config.IRemoteConfigProvider
import br.dev.singular.overview.data.remote.config.RemoteConfigKey
import kotlinx.serialization.json.Json
import javax.inject.Inject

interface ISuggestionRemoteDataSource {
    suspend fun getAll(): List<SuggestionDataModel>
}

class SuggestionRemoteDataSource @Inject constructor(
    private val provider: IRemoteConfigProvider
) : ISuggestionRemoteDataSource {

    override suspend fun getAll(): List<SuggestionDataModel> {
        val json = provider.getString(RemoteConfigKey.SUGGESTIONS_KEY)
        return Json.decodeFromString<List<SuggestionDataModel>>(json)
    }
}
