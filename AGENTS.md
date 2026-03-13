# Overview Project - AI Agent Guide

This document provides context and guidelines for AI agents working on the **Overview** project.

## Project Overview
**Overview** is an Android application designed to aggregate and navigate content across various streaming services. 
Users can search for media, filter by genre or type, and manage a collection of favorites.

## Architecture & Module Structure
The project follows **Clean Architecture** principles and is organized into a **multi-module** structure. 

### Module Hierarchy:
```text
overview/
├── app/                         → Entry point, DI configuration & Navigation.
│   ├── di/                      → Dependency Injection (Hilt) Modules.
│   ├── ui/                      → Main Activity and Navigation setup.
│   └── CustomApplication.kt     → Application class.   
├── domain/                      → Core Business Logic (Pure Kotlin/Java).
│   ├── model/                   → Domain Entities.
│   ├── usecase/                 → Business Logic Rules.
│   └── repository/              → Repository Interfaces (Contracts).
├── data/                        → Data Layer Implementation.
│   ├── repository/              → Domain Repository Implementations.
│   ├── remote/                  → Retrofit Services & API Data Models.
│   └── local/                   → Room Database & DataStore Implementation.
├── presentation/                → UI Layer (Jetpack Compose).
│   ├── ui/
│   │   ├── screens/             → Screen Composable (e.g., Home, Details).
│   │   └── components/          → Reusable UI Components (e.g., UiMediaGrid).
│   ├── viewmodel/               → ViewModels (exposing StateFlow).
│   └── tagging/                 → Analytics and User Tracking.
└── core/                        → Shared utilities and cross-cutting concerns.
    ├── remote/                  → Remote config utilities.
    └── crashlytics/             → Error reporting configuration.
```

## App module

This module has the **dirty main** of this project, it is a place that "glue" (with DI) everything together.
Today we are working to migrate this project to clean architecture, so this module has a lot of legacy code that we will refactor.

## Domain module

- This module contains the core business logic of the application.
- All implementation here **should be pure Kotlin**.

### Domain entities

Domain entities are data classes that represent the data business logic of the application.

> This class shound not have have a prefix or a posfix.

```kotlin

// a funny example of entity.
data class Apple(
  val id: Long,
  val description: String
)

```

### Repositories

Repositories are actually generic interfaces that perform only a single task:

```kotlin
interface GetById<T> {
  suspend fun getById(id: Long): T?
}
```

### Use Case

A use case represent a **single** business logic; they use a dependency inversion approach to interact with a repository through a generic interface.

```kotlin

// The Interface: Defines the "What"
interface IGetAppleByIdUseCase {
  /**
    * 'suspend' because fetching data might take time (network/database latency).
    * 'operator fun invoke' allows calling the class like a function: useCase(id).
    */
  suspend operator fun invoke(id: Long): UseCaseState<Apple?>
}

class GetAppleByIdUseCase(
  // The dependency that handles the actual data retrieval (concrete repository).
  private val getter: GetById<Apple> 
) : IGetAppleByIdUseCase {

  /**
  * 'runSafely' is a custom method that acts as a wrapper to catch exceptions.
  * Instead of crashing, it returns a controlled error state.
  */
  override suspend fun invoke(id: Long) = runSafely { getter.getById(id) }
}

```

## Data module

This module implements the repository interfaces defined in the `domain` module, coordinating data between local and remote sources.

### Data Handling
- **Remote:** Uses Retrofit for API calls. Response models usually have a `Response` or `DataModel` suffix.
- **Local:** Handles persistence (Room/DataStore).
- **Mappers:** Found in `util/mappers`, they convert data models into domain entities.

### Repository Implementation
Repositories coordinate data sources and return domain models.

```kotlin
class AppleRepository @Inject constructor(
  private val dataSource: IAppleRemoteDataSource
) : GetById<Apple> {
  override suspend fun getById(id: Long): Apple? {
    return when (val response = dataSource.getById(id)) {
      is DataResult.Success -> response.data.toDomain()
      is DataResult.Error -> null
    }
  }
}
```

## Presentation module

This module contains the UI layer built entirely with **Jetpack Compose**.

### State Management
- **UiState:** Uses a sealed class `UiState<T>` (Loading, Success, Error) to manage the UI state.
- **ViewModels:** Expose `StateFlow<UiState<T>>` to the screens.

### UI Models

```kotlin
data class AppleUiModel(
  val id: Long,
  val description: String
)
```

### View Mode Implementation

Here is how you should implement a `ViewMode` using the project's standards:

 ```kotlin
@HiltViewModel
class AppleDetailsViewMode @Inject constructor(
    private val useCase: IGetAppleByIdUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<AppleUiModel?>>(UiState.Loading())
    val uiState: StateFlow<UiState<AppleUiModel?>> = _uiState

    fun onLoad(id: Long) {
        _uiState.value = UiState.Loading()
        viewModelScope.launch(dispatcher) {
            _uiState.value = useCase.invoke(id).toUiState { it?.toUi() }
        }
    }

}
 ```

### UI Component

An Example of a UI Component:

```kotlin
@Composable
fun UiAppleCard(
    model: AppleUiModel,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Background
        ),
    ) {
        Box(Modifier.padding(dimensionResource(R.dimen.spacing_2x))) {
            UiText(text = model.description)
        }
    }
}

@Preview
@Composable
internal fun UiAppleCardPreview() {
    val model = AppleUiModel(id = 1, description = "Description")
    UiAppleCard(model)
}
```

## Core module

This module contains shared utilities.

- **Remote Config:** Wrappers for dynamic feature flags (Firebase).
- **Crashlytics:** Infrastructure for error reporting.
