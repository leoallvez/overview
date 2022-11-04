package io.github.leoallvez.take.ui

import io.github.leoallvez.take.data.api.response.MediaDetailResponse
import io.github.leoallvez.take.data.api.response.PersonResponse

typealias MediaUiState = UiState<MediaDetailResponse?>
typealias PersonUiState = UiState<PersonResponse?>

sealed class UiState <T> {
    class Error<T> : UiState<T>()
    class Loading<T> : UiState<T>()
    class Success<T>(val data: T) : UiState<T>()
}

sealed class PagingUiState {
    object Loading : PagingUiState()
    object Success : PagingUiState()
}
