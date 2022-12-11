package br.com.deepbyte.overview.ui

import br.com.deepbyte.overview.data.api.response.MediaDetailResponse
import br.com.deepbyte.overview.data.api.response.PersonDetails
import br.com.deepbyte.overview.data.repository.search.SearchResult

typealias MediaUiState = UiState<MediaDetailResponse?>
typealias PersonUiState = UiState<PersonDetails?>
typealias SearchUiState = UiState<SearchResult>

sealed class UiState <T> {
    class Loading<T> : UiState<T>()
    class Success<T>(val data: T) : UiState<T>()
    class Empty<T> : UiState<T>()
    class Error<T> : UiState<T>()
}
