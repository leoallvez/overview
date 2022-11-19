package br.com.deepbyte.overview.ui

import androidx.paging.PagingData
import br.com.deepbyte.overview.data.api.response.MediaDetailResponse
import br.com.deepbyte.overview.data.api.response.PersonResponse
import br.com.deepbyte.overview.data.model.MediaItem
import kotlinx.coroutines.flow.Flow

typealias MediaUiState = UiState<MediaDetailResponse?>
typealias PersonUiState = UiState<PersonResponse?>
typealias DiscoverUiState = UiState<Flow<PagingData<MediaItem>>>

sealed class UiState <T> {
    class Error<T> : UiState<T>()
    class Loading<T> : UiState<T>()
    class Success<T>(val data: T) : UiState<T>()
}
