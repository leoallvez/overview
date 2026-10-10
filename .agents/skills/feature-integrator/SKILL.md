---
name: feature-integrator
description: It integrates a Jetpack Compose screen with its ViewModel and registers it in the navigation graph. Use it when you have a screen and a ViewModel ready and need to glue them together.
metadata:
  author: Singular
  version: "1.2"
  keywords:
    - integration
    - screen
    - viewmodel
    - navigation
    - hilt
    - stateflow
---

## Core Workflow

- [ ] Step 1: Identify the Screen and ViewModel to be integrated. Refer
  to [screen-builder](../screen-builder/SKILL.md)
  and [viewmodel-builder](../viewmodel-builder/SKILL.md) if they are not ready yet. Ask the user for
  the feature name, navigation route name, and any arguments needed.
- [ ] Step 2: Create a plan detailing the new route function, how it will be registered in
  `AppNavHost.kt` and how the state/actions will be wired. Present it for approval.
- [ ] Step 3: Create the `YourRoute.kt` file in `br.dev.singular.overview.navigation.routes`
  (located in the `:app` module) with a `NavGraphBuilder.yourRoute(...)` extension function:
    - Add a `composable(route = Destination.YourRoute.route)` block.
    - If needed, add `arguments` using `navArgument` (or the shared ones in `NavArg`) and read them
      from the `NavBackStackEntry`.
    - If needed, add the enter/exit transitions.
- [ ] Step 4: Wire the ViewModel and Screen inside the `composable` block:
    - Inject the ViewModel using `hiltViewModel<YourViewModel>()`.
    - Collect the `uiState` using `viewModel.uiState.collectAsState().value`.
    - If using paging, collect with `viewModel.medias.collectAsLazyPagingItems()`.
    - Instantiate the `Actions` class, passing `navigation = navigation` and
      `handleIntent = viewModel::handleIntent`.
    - Ensure analytics tracking is correctly wired following
      the [analytics-tagger](../analytics-tagger/SKILL.md) skill.
- [ ] Step 5: Call the stateless Screen Composable with the collected state and configured actions.
- [ ] Step 6: Register the route in `AppNavHost.kt` (located in the `:app` module) by calling
  `yourRoute(navi)` inside the `NavHost` block.
- [ ] Step 7: Standardize the commit message using
  the [git-standardizer](../git-standardizer/SKILL.md) skill.

## Architecture Guidelines

Integration in this project happens in the `:app` module, split between `AppNavHost.kt` and the
`navigation/routes` package.

- **Centralized Navigation:** `AppNavHost` only lists the routes of the graph, keeping the
  `:presentation` module independent of the specific navigation implementation.
- **One Route per Destination:** Each destination lives in its own file in `navigation/routes` as a
  `NavGraphBuilder` extension function that owns the `composable`, its route, arguments, transitions
  and the screen-viewModel fusion (e.g., `searchRoute`, `profileRoute`).
- **Route Selection:** When a destination shows different screens (e.g., movie vs. TV show, login
  vs. favorites), the decision stays inside its route file, with each screen wired in a `private`
  composable.
- **State Collection:** Use `collectAsState().value` for standard `StateFlow` and
  `collectAsLazyPagingItems()` for paginated data.
- **Action Mapping:** Map ViewModel intents and navigation events (via `navigation`) directly in the
  `Actions` constructor.
- **Statelessness:** Screens must never receive the `ViewModel` instance. They only receive the data
  they need to display and the actions they can perform.

## Code Example

`navigation/routes/AppleRoute.kt`:

```kotlin
fun NavGraphBuilder.appleRoute(
    navigation: INavigationWrapper
) {
    composable(route = Destination.Apple.route) {
        val viewModel = hiltViewModel<AppleViewModel>()

        AppleScreen(
            uiState = viewModel.uiState.collectAsState().value,
            actions = AppleActions(
                navigation = navigation,
                handleIntent = viewModel::handleIntent
            )
        )
    }
}
```

`navigation/AppNavHost.kt`:

```kotlin
NavHost(...) {
    appleRoute(navi)
}
```

## Mandatory Rules

- **Location:** Each destination MUST be declared as a `NavGraphBuilder` extension in
  `br.dev.singular.overview.navigation.routes` and registered in
  `br.dev.singular.overview.navigation.AppNavHost.kt`.
- **Naming:** Use the `Route` suffix, one file per destination (e.g., `SearchRoute.kt` declaring
  `searchRoute`).
- **Hilt Injection:** Use `hiltViewModel<T>()` inside the `composable` block.
- **State Collection:** Use `collectAsState().value`.
- **Stateless Screens:** NEVER pass the `ViewModel` directly to the Screen Composable. Pass only
  `UiState` (and other states like `ScrollUiState`) and `Actions`.
- **Plan Approval:** You MUST NOT modify files until the user approves the integration plan in Step
    2.
