---
name: feature-integrator
description: It integrates a Jetpack Compose screen with its ViewModel and registers it in the navigation graph. Use it when you have a screen and a ViewModel ready and need to glue them together.
metadata:
  author: Singular
  version: "1.0"
  keywords:
  - integration
  - screen
  - viewmodel
  - navigation
  - hilt
  - stateflow
---

## Core Workflow

- [ ] Step 1: Identify the Screen and ViewModel to be integrated. Refer to [screen-builder](../screen-builder/SKILL.md) and [viewmodel-builder](../viewmodel-builder/SKILL.md) if they are not ready yet. Ask the user for the feature name, navigation route name, and any arguments needed.
- [ ] Step 2: Create a plan detailing how the navigation route will be registered in `AppNavHost.kt` and how the state/actions will be wired. Present it for approval.
- [ ] Step 3: Register the new route in `AppNavHost.kt` (located in the `:app` module):
    - Add a `composable(route = Destination.YourRoute.route)` block.
    - If needed, add `arguments` using `navArgument`.
- [ ] Step 4: Wire the ViewModel and Screen inside the `composable` block:
    - Inject the ViewModel using `hiltViewModel<YourViewModel>()`.
    - Collect the `uiState` using `viewModel.uiState.collectAsState().value`.
    - If using paging, collect with `viewModel.medias.collectAsLazyPagingItems()`.
    - Instantiate the `Actions` class, passing `navigation = navi` and `handleIntent = viewModel::handleIntent`.
    - Ensure analytics tracking is correctly wired following the [analytics-tagger](../analytics-tagger/SKILL.md) skill.
- [ ] Step 5: Call the stateless Screen Composable with the collected state and configured actions.
- [ ] Step 6: Standardize the commit message using the [git-standardizer](../git-standardizer/SKILL.md) skill.

## Architecture Guidelines

Integration in this project happens centrally in the `AppNavHost.kt` file.

- **Centralized Navigation:** All screen-viewModel fusions must occur within the `AppNavHost` to keep the `:presentation` module independent of the specific navigation implementation.
- **State Collection:** Use `collectAsState().value` for standard `StateFlow` and `collectAsLazyPagingItems()` for paginated data.
- **Action Mapping:** Map ViewModel intents and navigation events (via `navi`) directly in the `Actions` constructor.
- **Statelessness:** Screens must never receive the `ViewModel` instance. They only receive the data they need to display and the actions they can perform.

## Code Example

```kotlin
composable(route = Destination.Apple.route) {
    val viewModel = hiltViewModel<AppleViewModel>()

    AppleScreen(
        uiState = viewModel.uiState.collectAsState().value,
        actions = AppleActions(
            navigation = navi,
            handleIntent = viewModel::handleIntent
        )
    )
}
```

## Mandatory Rules

- **Location:** All integration changes MUST be made in `br.dev.singular.overview.navigation.AppNavHost.kt`.
- **Hilt Injection:** Use `hiltViewModel<T>()` inside the `composable` block.
- **State Collection:** Use `collectAsState().value`.
- **Stateless Screens:** NEVER pass the `ViewModel` directly to the Screen Composable. Pass only `UiState` (and other states like `ScrollUiState`) and `Actions`.
- **Plan Approval:** You MUST NOT modify files until the user approves the integration plan in Step 2.
