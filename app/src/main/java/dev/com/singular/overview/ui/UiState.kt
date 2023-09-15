package dev.com.singular.overview.ui

import dev.com.singular.overview.data.model.HomeData
import dev.com.singular.overview.data.model.media.Media
import dev.com.singular.overview.data.model.person.Person

typealias MediaUiState = UiState<Media?>
typealias PersonUiState = UiState<Person?>
typealias HomeUiState = UiState<HomeData>

sealed class UiState<T> {
    class Loading<T> : UiState<T>()
    class Success<T>(val data: T) : UiState<T>()
    class Error<T> : UiState<T>()
}
