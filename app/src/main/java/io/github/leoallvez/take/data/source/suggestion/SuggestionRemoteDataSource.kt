package io.github.leoallvez.take.data.source.suggestion

import io.github.leoallvez.take.data.model.Suggestion
import io.github.leoallvez.take.di.AbSuggestions
import io.github.leoallvez.take.abtest.AbTest
import javax.inject.Inject

class SuggestionRemoteDataSource @Inject constructor(
    @AbSuggestions
    private val _experiment: AbTest<List<Suggestion>>
) {
    fun get() = _experiment.execute()
}