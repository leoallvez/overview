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

    private val suggestionResultList: Flow<List<SuggestionResult>> = flow {
        val moviesResult = movieRepository.getData()
        val tvShowsResult = tvShowRepository.getData()

        val mergeResult = combine(suggestionOne, suggestionTwo) {
                elements -> elements.flatMap { it }
            }.first().sortedBy { it.order }
        
        emit(mergeResult)
    }

    fun getData(): Flow<List<SuggestionResult>> = flow {
        
        val moviesResult = movieRepository.getData()
        val tvShowsResult = tvShowRepository.getData()

        val mergeResult = combine(suggestionOne, suggestionTwo) { elements -> 
                elements.flatMap { it }
            }.first().sortedBy { it.order }
        
        emit(mergeResult)
    
    }
}
