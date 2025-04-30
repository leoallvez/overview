package br.dev.singular.overview.data.network.source

import br.dev.singular.overview.data.model.SuggestionDataModel

class SuggestionRemoteDataSource : RemoteConfigDataSource<List<SuggestionDataModel>> {

    override fun getAll(): List<SuggestionDataModel> {
        TODO("Not yet implemented")
    }

}
