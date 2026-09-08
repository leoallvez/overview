# ViewModel Build Guide

This reference demonstrates the standard pattern for creating a new ViewModel in the **Overview** project, following Clean Architecture and MVI-like interaction.

### 1. Standard ViewModel Implementation

For most screens, extend `ViewModel` and manage a single `UiState`.

```kotlin
@HiltViewModel
class AppleDetailsViewModel @Inject constructor(
    private val useCase: IGetAppleByIdUseCase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<AppleUiModel?>>(UiState.Loading())
    val uiState: StateFlow<UiState<AppleUiModel?>> = _uiState

    fun handleIntent(intent: AppleIntent) {
        when (intent) {
            is AppleIntent.Load -> onLoad(intent.id)
        }
    }

    private fun onLoad(id: Long) {
        _uiState.value = UiState.Loading()
        viewModelScope.launch(dispatcher) {
            // Using custom extension to transform UseCaseState to UiState
            _uiState.value = useCase(id).toUiState { it?.toUi() }
        }
    }
}
```

### 2. Paginated ViewModel Implementation

For screens displaying lists of media, extend `BaseMediaPagingViewModel`.

```kotlin
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTrendingUseCase: IGetTrendingUseCase
) : BaseMediaPagingViewModel() {

    override suspend fun onFetching(query: QueryUiState): UseCaseState<Page<Media>> {
        // BaseMediaPagingViewModel handles the Pager and exposes 'medias: Flow<PagingData<MediaUiModel>>'
        return getTrendingUseCase(query.toDomain())
    }
}
```

### 3. Key Components

- **UiState:** A sealed class representing `Loading`, `Success(data)`, and `Error(message)`.
- **Intents:** Defined in the `interaction` package of the screen, they represent user events.
- **Dispatchers:** Always injected to allow proper unit testing with `StandardTestDispatcher`.
- **Mappers:** Use `.toUi()` extension functions to convert Domain Models to UI Models within the ViewModel.

### Key Requirements:
1.  **Immutability:** StateFlow should always emit immutable UI models.
2.  **Error Handling:** Never let exceptions escape the `viewModelScope`. Use `toUiState` or similar wrappers.
3.  **Hilt:** Ensure the `@HiltViewModel` annotation is present and the constructor uses `@Inject`.
4.  **Separation of Concerns:** ViewModels should not contain Android-specific logic (like Context or Resources). Use `UiText` for translatable strings if needed.
