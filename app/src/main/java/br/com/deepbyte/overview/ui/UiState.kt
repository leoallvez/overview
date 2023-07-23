package br.com.deepbyte.overview.ui

import br.com.deepbyte.overview.data.model.person.Person
import br.com.deepbyte.overview.data.model.media.Media
import br.com.deepbyte.overview.data.model.provider.Streaming

typealias MediaUiState = UiState<Media?>
typealias PersonUiState = UiState<Person?>
typealias StreamingUiState = UiState<List<Streaming>?>

sealed class UiState<T> {
    class Loading<T> : UiState<T>()
    class Success<T>(val data: T) : UiState<T>()
    class Error<T> : UiState<T>()
}
