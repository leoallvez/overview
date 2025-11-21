package br.dev.singular.overview.presentation.ui.screens.favorites

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.repository.Page
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.media.IGetAllLocalMediasUseCase
import br.dev.singular.overview.presentation.model.MediaUiParam
import br.dev.singular.overview.presentation.model.MediaUiType
import br.dev.singular.overview.presentation.ui.screens.pagination.BaseMediaPagingViewMode
import br.dev.singular.overview.presentation.ui.utils.mappers.uiToDomain.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val useCase: IGetAllLocalMediasUseCase
) : BaseMediaPagingViewMode() {

    var isLoading by mutableStateOf(true)
        private set

    override suspend fun onFetching(param: MediaUiParam): UseCaseState<Page<Media>> {
        return useCase.invoke(param.copy(isLiked = true).toDomain())
    }

    fun onSelectType(type: MediaUiType) {
        onParamChanged(uiParam.value.copy(type = type))
    }

    fun onLoadingChanged(value: Boolean) {
        isLoading = value
    }
}


