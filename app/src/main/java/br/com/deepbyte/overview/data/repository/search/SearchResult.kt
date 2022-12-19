package br.com.deepbyte.overview.data.repository.search

import br.com.deepbyte.overview.data.api.response.ListResponse
import br.com.deepbyte.overview.data.model.media.Movie
import br.com.deepbyte.overview.data.model.media.TvShow
import br.com.deepbyte.overview.data.model.person.Person
import br.com.deepbyte.overview.data.source.DataResult

private typealias ListResult<T> = DataResult<ListResponse<T>>

data class SearchResult(
    val movies: ListResult<Movie>,
    val tvShows: ListResult<TvShow>,
    val persons: ListResult<Person>
) {

    val hasSuccess: Boolean
        get() = isSuccess(movies) || isSuccess(tvShows) || isSuccess(persons)

    private fun <T> isSuccess(result: DataResult<T>) = result is DataResult.Success
}
