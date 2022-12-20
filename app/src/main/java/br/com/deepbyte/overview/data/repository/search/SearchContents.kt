package br.com.deepbyte.overview.data.repository.search

import br.com.deepbyte.overview.data.api.response.ListResponse
import br.com.deepbyte.overview.data.model.media.Movie
import br.com.deepbyte.overview.data.model.media.TvShow
import br.com.deepbyte.overview.data.model.person.Person
import br.com.deepbyte.overview.data.source.DataResult

private typealias ListResult<T> = DataResult<ListResponse<T>>

class SearchContents(
    moviesResult: ListResult<Movie>,
    tvShowsResult: ListResult<TvShow>,
    personsResult: ListResult<Person>
) {

    val movies: List<Movie> by lazy { moviesResult.toList() }
    val tvShows: List<TvShow> by lazy { tvShowsResult.toList() }
    val persons: List<Person> by lazy { personsResult.toList() }

    val isNotEmpty: Boolean
        get() = movies.isNotEmpty() || tvShows.isNotEmpty() || persons.isNotEmpty()

    private fun <T> DataResult<ListResponse<T>>.toList(): List<T> {
        val isValid = this is DataResult.Success
        return (if (isValid) data?.results else listOf()) ?: listOf()
    }
}
