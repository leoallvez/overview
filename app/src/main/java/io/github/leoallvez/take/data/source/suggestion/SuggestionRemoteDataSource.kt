package io.github.leoallvez.take.data.source.suggestion

import io.github.leoallvez.take.data.model.Suggestion
import io.github.leoallvez.take.di.AbSuggestions
import io.github.leoallvez.take.experiment.AbExperiment
import javax.inject.Inject

class SuggestionRemoteDataSource @Inject constructor(
    @AbSuggestions
    val experiment: AbExperiment<List<Suggestion>>,
) {
    fun get() = experiment.execute()
}