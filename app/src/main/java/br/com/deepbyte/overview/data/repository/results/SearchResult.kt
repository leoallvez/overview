package br.com.deepbyte.overview.data.repository.results

import br.com.deepbyte.overview.data.source.DataResult
import br.com.deepbyte.overview.data.source.search.SearchMediasResult
import br.com.deepbyte.overview.data.source.search.SearchPersonsResult

data class SearchResult(
    private val moviesResult: SearchMediasResult,
    private val tvShowsResult: SearchMediasResult,
    private val personsResult: SearchPersonsResult
) {
    fun haveSuccessResult(): Boolean {

        val moviesAreSuccess = resultIsSuccess(moviesResult)
        val tvShowsAreSuccess = resultIsSuccess(tvShowsResult)
        val personsAreSuccess = resultIsSuccess(personsResult)

        return moviesAreSuccess || tvShowsAreSuccess || personsAreSuccess
    }

    private fun <T> resultIsSuccess(result: DataResult<T>) = result is DataResult.Success
}

