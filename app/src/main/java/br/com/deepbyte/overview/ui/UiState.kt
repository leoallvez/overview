package br.com.deepbyte.overview.ui

import br.com.deepbyte.overview.data.model.media.Media
import br.com.deepbyte.overview.data.model.person.Person
import br.com.deepbyte.overview.data.model.provider.StreamingsWrap

typealias MediaUiState = UiState<Media?>
typealias PersonUiState = UiState<Person?>
typealias StreamingUiState = UiState<StreamingsWrap>

sealed class UiState<T> {
    class Loading<T> : UiState<T>()
    class Success<T>(val data: T) : UiState<T>()
    class Error<T> : UiState<T>()
}
