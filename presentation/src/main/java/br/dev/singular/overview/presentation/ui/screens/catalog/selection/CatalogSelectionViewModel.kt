package br.dev.singular.overview.presentation.ui.screens.catalog.selection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dev.singular.overview.domain.usecase.ICatalogQueryStateUseCase
import br.dev.singular.overview.domain.usecase.ICatalogTooltipDismissedUseCase
import br.dev.singular.overview.domain.usecase.catalog.IGetAllCatalogUseCase
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.model.CatalogUiModel
import br.dev.singular.overview.presentation.model.CatalogUiState
import br.dev.singular.overview.presentation.model.QueryUiState
import br.dev.singular.overview.presentation.ui.screens.catalog.selection.interaction.CatalogSelectionIntent
import br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi.toUi
import br.dev.singular.overview.presentation.ui.utils.mappers.uiToDomain.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
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
class CatalogSelectionViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val getAllUseCase: IGetAllCatalogUseCase,
    private val queryStateUseCase: ICatalogQueryStateUseCase,
    private val catalogTooltipDismissedUseCase: ICatalogTooltipDismissedUseCase,
) : ViewModel() {

    private val _loadTrigger = MutableSharedFlow<Unit>(replay = 1)

    private val _tooltipDismissed = MutableStateFlow(true)
    val tooltipDismissed: StateFlow<Boolean> = _tooltipDismissed

    private var currentQuery: QueryUiState? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<UiState<CatalogUiState>> = _loadTrigger
        .flatMapLatest { loadData() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Loading()
        )

    fun handleIntent(intent: CatalogSelectionIntent) {
        when (intent) {
            is CatalogSelectionIntent.Load -> _loadTrigger.tryEmit(Unit)
            is CatalogSelectionIntent.Select -> onSelect(intent.catalog, intent.clearGenre)
            is CatalogSelectionIntent.DismissTooltip -> onDismissTooltip()
        }
    }

    private fun loadData(): Flow<UiState<CatalogUiState>> = flow {
        emit(UiState.Loading())

        emit(
            try {
                fetchCatalogState()
            } catch (e: Exception) {
                Timber.e(e)
                UiState.Error()
            }
        )
    }.flowOn(dispatcher)

    private suspend fun fetchCatalogState(): UiState<CatalogUiState> = coroutineScope {
        val queryDeferred = async { queryStateUseCase.get() }
        val optionsDeferred = async { getAllUseCase() }
        val tooltipDeferred = async { catalogTooltipDismissedUseCase.isDismissed() }

        val options = optionsDeferred.await().toUi().toImmutableList()
        currentQuery = queryDeferred.await()?.toUi()
        _tooltipDismissed.value = tooltipDeferred.await()

        if (options.isNotEmpty()) {
            UiState.Success(
                data = CatalogUiState(
                    selectedId = currentQuery?.catalog?.id,
                    options = options
                )
            )
        } else {
            UiState.Error()
        }
    }

    private fun onSelect(
        catalog: CatalogUiModel,
        clearGenre: Boolean
    ) {
        viewModelScope.launch(dispatcher) {
            val updatedQuery = if (clearGenre) {
                QueryUiState(catalog = catalog)
            } else {
                (currentQuery ?: QueryUiState()).copy(catalog = catalog)
            }
            queryStateUseCase.save(updatedQuery.toDomain())
        }
    }

    private fun onDismissTooltip() = viewModelScope.launch(dispatcher) {
        catalogTooltipDismissedUseCase.dismiss()
        _tooltipDismissed.value = true
    }
}
