---
name: snackbar-notifier
description: It shows feedback to the user through the project's snackbar (UiSnackbar) following the ViewModel flag → Screen effect → Actions flow, including its unit tests and animated snapshot tests. Use it when a feature needs to notify the user about the result of an action (success, error, undo).
metadata:
  author: Singular
  version: "1.0"
  keywords:
  - snackbar
  - feedback
  - notification
  - uisnackbar
  - animation
  - gif
  - snapshot
---

## Core Workflow

- [ ] Step 1: Identify what triggers the snackbar, its variant (`Close` or `Action`), style (`Success` or `Error`), icon, title and message. Ask the user for anything that is not clear.
- [ ] Step 2: Create a plan listing the files to change in each layer and present it for approval before moving to the next steps.
- [ ] Step 3: Add the texts to `strings.xml` in **every** locale folder of `presentation/src/main/res/`.
- [ ] Step 4: Expose the trigger from the ViewModel as a `StateFlow` flag (e.g. `favoriteAdded: StateFlow<Boolean>`) and add an intent that resets it (e.g. `DismissFavoriteAdded`). Refer to the [viewmodel-builder](../viewmodel-builder/SKILL.md) skill.
- [ ] Step 5: Add the callback to the feature `Actions` class:
    - Receive `onShowSnackbar: (UiSnackbarVisuals) -> Unit = {}` in the constructor.
    - Create one method that logs the interaction, shows the snackbar and sends the reset intent. Refer to the [analytics-tagger](../analytics-tagger/SKILL.md) skill.
- [ ] Step 6: Create a small effect Composable that resolves the strings and builds the `UiSnackbarVisuals` when the flag is `true`. Call it from the screen content, where the data is already loaded.
- [ ] Step 7: Wire it in `AppNavHost`: collect the flag and pass `onShowSnackbar = snackbarHostState::show`. Refer to the [feature-integrator](../feature-integrator/SKILL.md) skill.
- [ ] Step 8: Create a preview with `UiSnackbarPreview` that toggles the flag in a loop.
- [ ] Step 9: Create the unit tests (ViewModel, Actions and effect). See the [Unit Test Guide](references/UNIT-TEST-GUIDE.md).
- [ ] Step 10: Create the snapshot test using `gif` (animation), record it and check the recording. See the [Snapshot Test Guide](references/SNAPSHOT-TEST-GUIDE.md).
- [ ] Step 11: All tests must pass successfully.
- [ ] Step 12: Standardize the commit message using the [git-standardizer](../git-standardizer/SKILL.md) skill.

## Architecture Guidelines

The app has a single snackbar host. Features only describe **what** to show.

- **Single host:** `UiSnackbarHost` lives in the `Scaffold` of `MainActivity` and reads the `@Singleton` `UiSnackbarHostState`. Never add another host to a screen.
- **Unidirectional flow:** ViewModel flag → Screen parameter → effect Composable → `Actions` method → `snackbarHostState::show` (wired in `AppNavHost`).
- **Localized texts:** The `UiSnackbarVisuals` is built inside a Composable so that `stringResource` can be used. The ViewModel never builds the texts.
- **One-shot behavior:** The `Actions` method always sends the reset intent after showing, so the same event can be shown again later and is not repeated on recomposition.
- **Shared screens:** When two screens share a body (e.g. Movie and TV Show details), declare the method as `abstract` in the base `Actions` class and call the effect once in the shared body.

### Component options

| Option | Values | Default |
| :--- | :--- | :--- |
| Variant | `UiSnackbarVisuals.Close` (close button) or `UiSnackbarVisuals.Action` (text button, requires `actionText` and `onAction`) | - |
| `style` | `UiSnackbarStyle.Success` (green) or `UiSnackbarStyle.Error` (red) | `Success` |
| `icon` | Any `UiIconSource`, e.g. `UiIconSource.vector(Lucide.Heart)`. It is tinted with the style accent color. | `style.defaultIcon` |
| `durationMillis` | Time on screen before the automatic dismissal | `4000L` |

Up to 3 snackbars are stacked; the oldest one is removed when a fourth is shown.

## Code Examples

Reference implementation: the "added to favorites" snackbar in `ui/screens/media/`.

### ViewModel flag and reset intent
```kotlin
private val _favoriteAdded = MutableStateFlow(false)
val favoriteAdded: StateFlow<Boolean> = _favoriteAdded.asStateFlow()

fun handleIntent(intent: MovieDetailsIntent) {
    viewModelScope.launch(dispatcher) {
        when (intent) {
            is MovieDetailsIntent.Like -> onLike(intent.media)
            is MovieDetailsIntent.DismissFavoriteAdded -> _favoriteAdded.update { false }
        }
    }
}
```

### Actions
```kotlin
fun onFavoriteAdded(visuals: UiSnackbarVisuals) {
    TagManager.logInteraction(customPath = tagPath, detail = "favorite-added")
    onShowSnackbar(visuals)
    handleIntent(MovieDetailsIntent.DismissFavoriteAdded)
}
```

### Effect Composable
```kotlin
@Composable
internal fun MediaFavoriteAddedEffect(
    favoriteAdded: Boolean,
    mediaTitle: String,
    onShow: (UiSnackbarVisuals) -> Unit
) {
    val title = stringResource(R.string.favorite_added_title)

    LaunchedEffect(favoriteAdded) {
        if (favoriteAdded) {
            onShow(
                UiSnackbarVisuals.Close(
                    title = title,
                    message = mediaTitle,
                    icon = UiIconSource.vector(Lucide.Heart)
                )
            )
        }
    }
}
```

### Navigation wiring
```kotlin
MovieDetailsScreen(
    favoriteAdded = viewModel.favoriteAdded.collectAsState().value,
    actions = rememberMovieDetailsActions(
        navigation = navi,
        handleIntent = viewModel::handleIntent,
        onShowSnackbar = snackbarHostState::show
    )
)
```

### Preview with animation loop
```kotlin
@UiScreenPreview
@Composable
internal fun MovieDetailsScreenFavoriteAddedPreview() {
    // Active Interactive mode to see the snackbar
    var favoriteAdded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (true) {
            favoriteAdded = true
            delay(5_000.milliseconds)
            favoriteAdded = false
            delay(1_000.milliseconds)
        }
    }

    UiSnackbarPreview { hostState ->
        MovieDetailsScreen(
            movieId = 1L,
            uiState = UiState.Success(data = fakeMovieDetails()),
            favoriteAdded = favoriteAdded,
            actions = rememberMovieDetailsActions(onShowSnackbar = hostState::show)
        )
    }
}
```

## References

For detailed patterns and testing strategies, refer to:

- [Unit Test Guide](references/UNIT-TEST-GUIDE.md): ViewModel flag, `Actions` and effect Composable tests.
- [Snapshot Test Guide](references/SNAPSHOT-TEST-GUIDE.md): Step by step to create an animated (`gif`) snapshot test for a screen that shows a snackbar.

## Mandatory Rules

- **No New Host:** NEVER add `UiSnackbarHost` or a Material `SnackbarHost` to a screen. Use the global one.
- **No Hardcoded Texts:** Title, message and action text MUST come from `strings.xml`, translated in every locale.
- **No Host State in ViewModels:** DO NOT inject `UiSnackbarHostState` into a ViewModel. Expose a flag and let the screen build the visuals.
- **Always Reset:** The `Actions` method MUST send the reset intent after showing the snackbar.
- **Preview Wrapper:** Previews that show a snackbar MUST use `UiSnackbarPreview` and toggle the flag in a loop.
- **Animated Snapshot:** Snapshot tests of a snackbar on a screen MUST use `gif`, never `snapshot`. A static snapshot is taken before the snackbar appears.
- **Plan Approval:** You MUST NOT modify files until the user approves the plan in Step 2.
- **Tests Success:** All new tests MUST pass before completing the task.
