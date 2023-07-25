package br.com.deepbyte.overview.data.source.suggestion

import br.com.deepbyte.overview.abtesting.RemoteConfig
import br.com.deepbyte.overview.data.model.Suggestion
import br.com.deepbyte.overview.di.SuggestionsRemote
import javax.inject.Inject

class SuggestionRemoteDataSource @Inject constructor(
    @SuggestionsRemote
    private val _remoteConfig: RemoteConfig<List<Suggestion>>
) {
    fun getItems() = _remoteConfig.execute()
}
