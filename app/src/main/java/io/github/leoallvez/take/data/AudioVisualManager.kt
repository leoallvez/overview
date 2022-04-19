package io.github.leoallvez.take.data

import io.github.leoallvez.take.data.model.SuggestionResult
import io.github.leoallvez.take.data.repository.movie.MovieRepository
import io.github.leoallvez.take.data.repository.tvshow.TvShowRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class AudioVisualManager @Inject constructor(
    private val movieRepository: MovieRepository,
    private val tvShowRepository: TvShowRepository
) {

    val getData: Flow<List<SuggestionResult>> = flow {
        val result1 = movieRepository.getData()
        val result2 = tvShowRepository.getData()
        val result3 = merge(result1, result2).first()

        emit(result3)
    }
}