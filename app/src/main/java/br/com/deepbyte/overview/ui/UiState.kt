package br.com.deepbyte.overview.ui

import br.com.deepbyte.overview.data.model.person.PersonDetails
import br.com.deepbyte.overview.data.model.media.Media
import br.com.deepbyte.overview.data.repository.search.SearchResult

typealias MediaUiState = UiState<Media?>
typealias PersonUiState = UiState<PersonDetails?>

sealed class UiState <T> {
    class Loading<T> : UiState<T>()
    class Success<T>(val data: T) : UiState<T>()
    class Error<T> : UiState<T>()
}

typealias SearchUiState = SearchState<SearchResult>

sealed class SearchState <T> {
    class NotStated<T> : SearchState<T>()
    class Loading<T> : SearchState<T>()
    class Success<T>(val data: T) : SearchState<T>()
    class Empty<T> : SearchState<T>()
}
