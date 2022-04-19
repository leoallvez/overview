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

    fun getData(): Flow<List<SuggestionResult>> = flow {
        
        val movies = movieRepository.getData()
        val tvShows = tvShowRepository.getData()

        val merged = combine(movies, tvShows) { e -> e.flatMap { it } }
            .first()
            .sortedBy { it.order }
        
        emit(merged)
    }

}
