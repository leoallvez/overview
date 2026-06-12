package br.dev.singular.overview.presentation.ui.screens.genre

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.usecase.ICatalogQueryStateUseCase
import br.dev.singular.overview.domain.usecase.genre.IFetchGenresByTypeUseCase
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.model.GenreUiModel
import br.dev.singular.overview.presentation.model.GenreUiState
import br.dev.singular.overview.presentation.model.QueryUiState
import br.dev.singular.overview.presentation.ui.screens.genre.interaction.GenreSelectionIntent
import br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi.toUi
import br.dev.singular.overview.presentation.ui.utils.mappers.uiToDomain.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GenreSelectionViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val fetchGenresUseCase: IFetchGenresByTypeUseCase,
    private val queryStateUseCase: ICatalogQueryStateUseCase
) : ViewModel() {

    private val _loadTrigger = MutableSharedFlow<Unit>(replay = 1)

    private var currentQuery: QueryUiState? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<UiState<GenreUiState>> = _loadTrigger
        .flatMapLatest { loadData() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Loading()
        )

    fun handleIntent(intent: GenreSelectionIntent) {
        when (intent) {
            is GenreSelectionIntent.Load -> _loadTrigger.tryEmit(Unit)
            is GenreSelectionIntent.Select -> onSelect(intent.genre)
        }
    }

    private fun loadData(): Flow<UiState<GenreUiState>> = flow {
        emit(UiState.Loading())

        emit(
            try {
                val query = queryStateUseCase.get()?.toUi()
                currentQuery = query

                val type = query?.type?.toDomain() ?: MediaType.MOVIE
                val genres = fetchGenresUseCase(type).toUi()

                UiState.Success(
                    data = GenreUiState(
                        selectedId = query?.genre?.id,
                        options = genres
                    )
                )
            } catch (e: Exception) {
                Timber.e(e)
                UiState.Error()
            }
        )
    }.flowOn(dispatcher)

    private fun onSelect(genre: GenreUiModel?) = viewModelScope.launch(dispatcher) {
        queryStateUseCase.save(currentQuery?.copy(genre = genre)?.toDomain())
    }
}
