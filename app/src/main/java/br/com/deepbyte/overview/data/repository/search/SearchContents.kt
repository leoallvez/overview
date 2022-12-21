package br.com.deepbyte.overview.data.repository.search

import br.com.deepbyte.overview.data.api.response.ListResponse
import br.com.deepbyte.overview.data.model.media.Movie
import br.com.deepbyte.overview.data.model.media.TvShow
import br.com.deepbyte.overview.data.model.person.Person
import br.com.deepbyte.overview.data.source.DataResult
import br.com.deepbyte.overview.util.toList

private typealias ListResult<T> = DataResult<ListResponse<T>>

class SearchContents(
    movies: ListResult<Movie>,
    tvShows: ListResult<TvShow>,
    persons: ListResult<Person>
) {

    val movies: List<Movie> by lazy { movies.toList() }
    val tvShows: List<TvShow> by lazy { tvShows.toList() }
    val persons: List<Person> by lazy { persons.toList() }

    val isNotEmpty: Boolean
        get() = movies.isNotEmpty() || tvShows.isNotEmpty() || persons.isNotEmpty()
}
