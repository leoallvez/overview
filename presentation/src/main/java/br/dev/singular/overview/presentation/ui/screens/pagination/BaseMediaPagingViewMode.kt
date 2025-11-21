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

abstract class BaseMediaPagingViewMode : ViewModel() {

    private val _uiParam = MutableStateFlow(MediaUiParam())
    val uiParam: StateFlow<MediaUiParam> = _uiParam

    @OptIn(ExperimentalCoroutinesApi::class)
    val medias: Flow<PagingData<MediaUiModel>> = _uiParam
        .flatMapLatest { param -> onCreatePagingData(param) }
        .map { pagingData -> pagingData.map { media -> media.toUi() } }
        .cachedIn(viewModelScope)

    fun onParamChanged(param: MediaUiParam) {
        _uiParam.value = param
    }

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

    protected abstract suspend fun onFetching(param: MediaUiParam): UseCaseState<Page<Media>>

}
