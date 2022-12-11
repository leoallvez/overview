package br.com.deepbyte.overview.data.repository.search

import br.com.deepbyte.overview.data.api.response.ListResponse
import br.com.deepbyte.overview.data.api.response.Person
import br.com.deepbyte.overview.data.model.MediaItem
import br.com.deepbyte.overview.data.source.DataResult

private typealias MediaResult = DataResult<ListResponse<MediaItem>>

data class SearchResult(
    private val moviesResult: MediaResult,
    private val tvShowsResult: MediaResult,
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
