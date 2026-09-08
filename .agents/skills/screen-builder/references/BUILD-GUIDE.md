# Screen Build Guide

This reference demonstrates the standard pattern for creating a new screen in the **Overview** project, focusing on the stateless UI and Interaction Layer.

### 1. Interaction Layer Contract

Define the UI event model and the actions the screen can perform. This separates "what the user did" (Intent) from "how it's handled" (Actions).

**Intent:**
```kotlin
sealed class AppleDetailIntent {
    class Load : AppleDetailIntent()
    data class Select(val model: AppleUiModel?) : AppleDetailIntent()
}
```

**Actions:**
```kotlin
@Immutable
data class AppleDetailActions(
    private val navigation: INavigationWrapper? = null,
    private val handleIntent: (intent: AppleDetailIntent) -> Unit = {},
) {

    val tagPath: String = "/apple-details"

    fun onLoad() = handleIntent(AppleDetailIntent.Load())

    fun navigateBack() = navigation?.popBackStack()
}
```

### 2. Screen Composable

The main screen function should be stateless and orchestrate `UiScaffold` and its content.

```kotlin
/**
 * Main entry point for the Apple Detail Screen.
 *
 * This screen displays the detailed information of a specific apple, handling loading,
 * success, and error states through the provided [uiState].
 *
 * @param uiState The current [UiState] of the screen, holding an [AppleUiModel].
 * @param actions The [AppleDetailActions] used to handle user interactions and navigation.
 */
@Composable
fun AppleDetailScreen(
    uiState: UiState<AppleUiModel>,
    actions: AppleDetailActions
) {
    LaunchedEffect(Unit) { actions.onLoad() }
    UiScaffold(
        topBar = {
            UiTopAppBar(
                title = stringResource(R.string.apple_details),
                onBack = actions::navigateBack
            )
        },
    ) { padding ->
        UiStateResult(
            uiState = uiState,
            tagPath = actions.tagPath,
            onRefresh = { actions.onLoad() },
            modifier = Modifier.padding(padding),
            loadingContent = { UiAppleDetailSkeleton(tagPath = actions.tagPath) },
        ) { data ->
            // ui components and logic here
        }
    }
}

/**
 * A skeleton loading view for the Apple Detail Screen.
 *
 * This component provides a visual placeholder while the apple details are being fetched.
 *
 * @param tagPath The analytics tag path for tracking the screen view.
 * @param modifier The [Modifier] to be applied to the skeleton layout.
 */
@Composable
internal fun UiAppleDetailSkeleton(
    tagPath: String,
    modifier: Modifier = Modifier,
) {
    TrackScreenView(tagPath)
    // skeleton components here
}
```

### 3. Screen Previews

Use `@UiScreenPreview` to verify the UI. Previews should demonstrate all major states.

```kotlin
@UiScreenPreview
@Composable
internal fun AppleDetailScreenSuccessPreview() {
    val model = AppleUiModel(id = 1, description = "Preview Apple")
    AppleDetailScreen(
        uiState = UiState.Success(model),
        actions = AppleDetailActions()
    )
}

@UiScreenPreview
@Composable
internal fun AppleDetailScreenLoadingPreview() {
    AppleDetailScreen(
        uiState = UiState.Loading(),
        actions = AppleDetailActions()
    )
}

@UiScreenPreview
@Composable
internal fun AppleDetailScreenErrorPreview() {
    AppleDetailScreen(
        uiState = UiState.Error(),
        actions = AppleDetailActions()
    )
}
```

### Key Requirements:
1.  **Statelessness:** The screen Composable accepts state and emits events. It does not manage business state.
2.  **Scaffold Usage:** Always use `UiScaffold` to maintain consistency with system bars and the app's overall structure.
3.  **Interaction Decoupling:** Use the `Actions` pattern to keep the Composable's signature clean and facilitate testing/previews.
4.  **Internal Content:** Extract the main content area into an `internal` function to improve readability and allow targeted previews/tests.
