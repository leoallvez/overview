---
name: viewmodel-builder
description: It builds a new ViewModel following the project's architecture (Hilt, UiState, Intent handling, and UseCase integration). Use it when users want to create a new ViewModel for a feature or screen.
metadata:
  author: Singular
  version: "1.0"
  keywords:
  - viewmodel
  - hilt
  - uistate
  - intent
  - coroutines
  - paging
---

## Core Workflow

- [ ] Step 1: Identify the feature name, the required data state (UiState), and **which UseCase(s)** will be used. Refer to the [usecase-builder](../usecase-builder/SKILL.md) skill if new ones are needed. Ask the user for these details.
- [ ] Step 2: Create a plan detailing the ViewModel's state, intents, and dependencies. Present it for approval before moving to the next steps.
- [ ] Step 3: Create a new class in `br.dev.singular.overview.presentation.viewmodel.<feature_name>`.
- [ ] Step 4: Annotate with `@HiltViewModel` and inject required UseCases and `CoroutineDispatcher`.
- [ ] Step 5: Define the `UiState` type (e.g., `UiState<MyDataUiModel>`) and initialize a `MutableStateFlow` with `UiState.Loading()`.
- [ ] Step 6: Implement the `handleIntent(intent: MyIntent)` method to bridge UI events to business logic.
- [ ] Step 7: Create private functions for each intent, using `viewModelScope.launch(dispatcher)` for asynchronous operations.
- [ ] Step 8: Use `toUiState` and `toUi()` mappers to transform domain data into UI models. Refer to the [mapper-builder](../mapper-builder/SKILL.md) skill for mapper implementation.
- [ ] Step 9: If the screen requires pagination, extend `BaseMediaPagingViewModel`.
- [ ] Step 10: Create a unit test for the ViewModel in `presentation/src/test/java/...` using MockK and Coroutines test utilities.
- [ ] Step 11: All new tests must pass successfully.
- [ ] Step 12: Standardize the commit message using the [git-standardizer](../git-standardizer/SKILL.md) skill.

## Architecture Guidelines

ViewModels are the bridge between the UI and the Domain layer.

- **State Management:** Always use `UiState<T>` (Loading, Success, Error). Expose state via `StateFlow`.
- **Threading:** Use the injected `CoroutineDispatcher` (usually `IO` or `Default`) for all `viewModelScope` launches.
- **Dependency Inversion:** UseCases MUST be injected via interfaces.
- **Intent-Driven:** All UI interactions must trigger a `handleIntent` call. Avoid public functions for specific actions unless necessary for complex lifecycle events.
- **Thin ViewModels:** Keep ViewModels focused on state coordination. Move complex data transformations to UI Mappers and business logic to UseCases.

## References

For detailed patterns, code examples, and testing strategies, refer to:

- [ViewModel Build Guide](references/BUILD-GUIDE.md): Implementation details and Hilt setup.
- [ViewModel Unit Test Guide](references/UNIT-TEST-GUIDE.md): Unit testing pattern with MockK and Coroutines.

## Mandatory Rules

- **UseCase Identification:** You MUST ask the user which UseCase(s) will be used before starting the implementation.
- **Plan Approval:** You MUST NOT modify files until the user approves the implementation plan in Step 2.
- **Hilt Annotation:** Every ViewModel MUST have `@HiltViewModel`.
- **Dispatcher Injection:** DO NOT use `Dispatchers.IO` directly. Use `@IoDispatcher` (or equivalent qualifier) injected in the constructor.
- **State Initialization:** The initial state should typically be `UiState.Loading()`.
- **Safe Execution:** Use the project's result handling wrappers (like `runSafely` or `UseCaseState`) to avoid crashing on exceptions.
- **Testing:** Every ViewModel must have a corresponding unit test covering all Intents and state transitions.
