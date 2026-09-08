---
name: screen-builder
description: It builds a new Jetpack Compose screen following the project's architecture (stateless Composable, UiIntent, Actions, and Previews). Use it when users want to create a new UI screen/feature from scratch.
metadata:
  author: Singular
  version: "1.0"
  keywords:
  - ui
  - screen
  - intent
  - actions
  - layout
  - preview
  - uistate
---

## Core Workflow

- [ ] Step 1: Identify the screen name, purpose, and visual structure (e.g., TopBar, Content, Pagination). Create a plan and present it for approval before moving to the next steps.
- [ ] Step 2: Create a new package inside `br.dev.singular.overview.presentation.ui.screens.<feature_name>`.
- [ ] Step 3: Define the Interaction Contract:
    - Create `interaction/<ScreenName>Intent.kt` (Sealed class representing UI events/user actions).
    - Create `interaction/<ScreenName>Actions.kt` (Immutable data class holding lambda callbacks and navigation triggers).
- [ ] Step 4: Create the `<ScreenName>Screen.kt`:
    - Define a stateless Composable for the screen.
    - Accept `uiState: UiState<T>` and `actions: <ScreenName>Actions`.
    - Use `Actions` to delegate all events.
    - Use `UiScaffold` as the root container to manage system bars and consistent spacing.
    - Implement analytics tracking using the [analytics-tagger](../analytics-tagger/SKILL.md) skill.
- [ ] Step 5: Implement internal layout Composables (e.g., `...Content`) to keep the main screen function clean and testable.
- [ ] Step 6: Create comprehensive internal previews using `@UiScreenPreview`. Show Success, Loading (Skeletons), and Error states.
- [ ] Step 7: Create a UI unit test file using `ComposeTestRule` to verify that clicking UI elements triggers the correct `Action`.
- [ ] Step 8: Create a snapshot test class extending `UiSnapshotTest` to verify visual consistency across states.
- [ ] Step 9: All new tests (unit and snapshot) should pass successfully.
- [ ] Step 10: Integrate the screen into the navigation graph using the [feature-integrator](../feature-integrator/SKILL.md) skill.
- [ ] Step 11: Standardize the commit message using the [git-standardizer](../git-standardizer/SKILL.md) skill.

## Architecture Guidelines

Screens are orchestrators of the UI, focusing on layout and interaction flow.

- **Interaction Layer:** The screen MUST be stateless. All user interactions (clicks, scrolls) should be converted into `Intents` and handled via the `Actions` class.
- **Data Input:** Screens MUST receive data via `UiState<T>`. Any formatting or business logic must be completed before reaching the Composable.
- **Scaffold Consistency:** Always use `UiScaffold`. It provides the correct structure for `TopAppBar`, `FloatingActionButton`, and `SnackBar`.
- **Statelessness:** Avoid `remember`ed state within the Screen Composable for anything other than transient UI animations.

## Code Examples (Coherent with AGENTS.md)

### Intent & Actions
```kotlin
sealed class AppleIntent {
    data class Load(val id: Long) : AppleIntent()
}

@Immutable
data class AppleActions(
    val handleIntent: (AppleIntent) -> Unit = {}
) {
    fun onLoad(id: Long) = handleIntent(AppleIntent.Load(id))
}
```

## References

For detailed patterns, code examples, and testing strategies, refer to:

- [Screen Build Guide](references/BUILD-GUIDE.md): Layout structure, Scaffold usage, and Action pattern.
- [Snapshot Test Guide](references/SNAPSHOT-TEST-GUIDE.md): Visual regression testing for screens.
- [Unit Test Guide](../component-builder/references/UNIT-TEST-GUIDE.md): Unit testing for Compose components (reusable for screens).

## Mandatory Rules

- **Internal Visibility:** Screen content Composables should be `internal`. The main entry point should be public only if needed by navigation.
- **UiScaffold Usage:** Every screen MUST use `UiScaffold`. Ensure `innerPadding` is applied to the content.
- **Modifier First:** The first optional parameter of every Composable MUST be `Modifier`.
- **Stateless Previews:** Previews should use static fake data and mock `handleIntent` implementation in `Actions`.
- **KDoc Documentation:** Provide a KDoc block for every screen and internal content Composable.
