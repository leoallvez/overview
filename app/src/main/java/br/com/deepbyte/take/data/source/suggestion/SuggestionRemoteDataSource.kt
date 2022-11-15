package br.com.deepbyte.take.data.source.suggestion

import br.com.deepbyte.take.abtest.AbTest
import br.com.deepbyte.take.data.model.Suggestion
import br.com.deepbyte.take.di.AbSuggestions
import javax.inject.Inject

class SuggestionRemoteDataSource @Inject constructor(
    @AbSuggestions
    private val _experiment: AbTest<List<Suggestion>>
) {
    fun get() = _experiment.execute()
}
