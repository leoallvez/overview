package br.com.deepbyte.overview.data.source.suggestion

import br.com.deepbyte.overview.abtest.AbTest
import br.com.deepbyte.overview.data.model.Suggestion
import br.com.deepbyte.overview.di.AbSuggestions
import javax.inject.Inject

class SuggestionRemoteDataSource @Inject constructor(
    @AbSuggestions
    private val _experiment: AbTest<List<Suggestion>>
) {
    fun get() = _experiment.execute()
}
