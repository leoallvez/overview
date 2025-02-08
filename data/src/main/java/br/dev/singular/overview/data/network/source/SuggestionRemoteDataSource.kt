package br.dev.singular.overview.data.network.source

import br.dev.singular.overview.core.remote.RemoteConfigKey
import br.dev.singular.overview.core.remote.RemoteConfigProvider
import br.dev.singular.overview.data.model.SuggestionDataModel
import kotlinx.serialization.json.Json
import javax.inject.Inject

interface ISuggestionRemoteDataSource {
    suspend fun getAll(): List<SuggestionDataModel>
}

class SuggestionRemoteDataSource @Inject constructor(
    private val provider: RemoteConfigProvider
) : ISuggestionRemoteDataSource {

    override suspend fun getAll(): List<SuggestionDataModel> {
        val json = provider.getString(RemoteConfigKey.SUGGESTIONS_KEY)
        return Json.decodeFromString<List<SuggestionDataModel>>(json)
    }
}
