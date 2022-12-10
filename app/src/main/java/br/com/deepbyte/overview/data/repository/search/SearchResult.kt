package br.com.deepbyte.overview.data.repository.search

import br.com.deepbyte.overview.data.api.response.SearchMediaResponse
import br.com.deepbyte.overview.data.api.response.SearchPersonResponse
import br.com.deepbyte.overview.data.source.DataResult

data class SearchResult(
    private val moviesResult: DataResult<SearchMediaResponse>,
    private val tvShowsResult: DataResult<SearchMediaResponse>,
    private val personsResult: DataResult<SearchPersonResponse>
) {
    fun haveSuccessResult(): Boolean {

        val moviesAreSuccess = resultIsSuccess(moviesResult)
        val tvShowsAreSuccess = resultIsSuccess(tvShowsResult)
        val personsAreSuccess = resultIsSuccess(personsResult)

        return moviesAreSuccess || tvShowsAreSuccess || personsAreSuccess
    }

    private fun <T> resultIsSuccess(result: DataResult<T>) = result is DataResult.Success
}
