package br.dev.singular.overview.presentation.ui.screens.pagination

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.repository.Page
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.presentation.BuildConfig
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.model.MediaUiParam
import br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi.toUi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/**
 * An abstract ViewModel that provides a base for screens that display paginated media.
 *
 * This class handles the logic for fetching and managing paginated media data using the Paging 3 library.
 * Subclasses must implement the [onFetching] method to provide the actual data fetching logic.
 */
abstract class BaseMediaPagingViewMode : ViewModel() {

    private val _uiParam = MutableStateFlow(MediaUiParam())
    /**
     * The current UI parameters for fetching media.
     */
    val uiParam: StateFlow<MediaUiParam> = _uiParam

    /**
     * A flow of paginated media data to be displayed on the UI.
     *
     * This flow is driven by changes to [uiParam] and will automatically fetch new data
     * when the parameters change.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val medias: Flow<PagingData<MediaUiModel>> = _uiParam
        .flatMapLatest { param -> onCreatePagingData(param) }
        .map { pagingData -> pagingData.map { media -> media.toUi() } }
        .cachedIn(viewModelScope)

    /**
     * Updates the UI parameters for fetching media.
     *
     * @param param The new set of parameters.
     */
    fun onParamChanged(param: MediaUiParam) {
        _uiParam.value = param
    }

    /**
     * Triggers a refresh of the media data.
     */
    fun onReload() = _uiParam.update { it.refresh() }

    private fun onCreatePagingData(param: MediaUiParam): Flow<PagingData<Media>> {
        return Pager(
            config = PagingConfig(
                pageSize = BuildConfig.PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MediaPagingLoader { page -> onFetching(param.copy(page = page)) }
            }
        ).flow
    }

    /**
     * Fetches a page of media data based on the given parameters.
     *
     * This method is called by the [MediaPagingLoader] to load pages of data.
     *
     * @param param The parameters for the media request, including the page number.
     * @return A [UseCaseState] containing the fetched [Page] of [Media] or an error.
     */
    protected abstract suspend fun onFetching(param: MediaUiParam): UseCaseState<Page<Media>>

}
