package br.com.deepbyte.overview.data.repository.search

import br.com.deepbyte.overview.data.api.response.ListResponse
import br.com.deepbyte.overview.data.model.media.Movie
import br.com.deepbyte.overview.data.model.media.TvShow
import br.com.deepbyte.overview.data.model.person.Person
import br.com.deepbyte.overview.data.source.DataResult

data class SearchResult(
    private val moviesResult: DataResult<ListResponse<Movie>>,
    private val tvShowsResult: DataResult<ListResponse<TvShow>>,
    private val personsResult: DataResult<ListResponse<Person>>
) {
    fun haveSuccessResult(): Boolean {

        val moviesAreSuccess = resultIsSuccess(moviesResult)
        val tvShowsAreSuccess = resultIsSuccess(tvShowsResult)
        val personsAreSuccess = resultIsSuccess(personsResult)

        return moviesAreSuccess || tvShowsAreSuccess || personsAreSuccess
    }

    private fun <T> resultIsSuccess(result: DataResult<T>) = result is DataResult.Success
}
