package br.com.deepbyte.overview.ui

import br.com.deepbyte.overview.data.api.response.MediaDetailResponse
import br.com.deepbyte.overview.data.api.response.PersonResponse
import br.com.deepbyte.overview.data.repository.results.SearchResult

typealias MediaUiState = UiState<MediaDetailResponse?>
typealias PersonUiState = UiState<PersonResponse?>
typealias SearchUiState = UiState<SearchResult>

sealed class UiState <T> {
    class Loading<T> : UiState<T>()
    class Success<T>(val data: T) : UiState<T>()
    class Empty<T> : UiState<T>()
    class Error<T> : UiState<T>()
}
