# Snackbar Unit Test Guide

This reference provides examples for testing a snackbar flow in the **Overview** project. Each layer has its own test: the ViewModel flag, the `Actions` method and the effect Composable.

## ViewModel: flag transitions

Cover when the flag turns on, when it must stay off and the reset intent.

### Example: `MovieDetailsViewModelTest.kt`

```kotlin
@Test
fun `Like intent should not notify favorite added when the media is unliked`() = runTest {
    // arrange
    coEvery { delegate.toggleLike(any()) } returns UseCaseState.Success(false)

    // act
    sut.handleIntent(MovieDetailsIntent.Like(createMovieDetailsMock().toUi(isLiked = true)))

    // assert
    sut.favoriteAdded.value shouldBeEqualTo false
}

@Test
fun `DismissFavoriteAdded intent should reset favoriteAdded`() = runTest {
    // arrange
    coEvery { delegate.toggleLike(any()) } returns UseCaseState.Success(true)
    sut.handleIntent(MovieDetailsIntent.Like(createMovieDetailsMock().toUi(isLiked = false)))

    // act
    sut.handleIntent(MovieDetailsIntent.DismissFavoriteAdded)

    // assert
    sut.favoriteAdded.value shouldBeEqualTo false
}
```

## Actions: log, show and reset

Collect the snackbars and intents in lists and mock `TagManager`.

### Example: `MovieDetailsActionsTest.kt`

```kotlin
private val intents = mutableListOf<MovieDetailsIntent>()
private val snackbars = mutableListOf<UiSnackbarVisuals>()
private val sut = MovieDetailsActions(
    navigation = navigation,
    handleIntent = { intents.add(it) },
    onShowSnackbar = { snackbars.add(it) }
)

@Before
fun setup() {
    mockkObject(TagManager)
    justRun { TagManager.logInteraction(any(), any()) }
}

@After
fun tearDown() {
    unmockkObject(TagManager)
}

@Test
fun `onFavoriteAdded should log interaction, show the snackbar and dismiss the notice`() {
    // Given
    val visuals: UiSnackbarVisuals = mockk()

    // When
    sut.onFavoriteAdded(visuals)

    // Then
    verify(exactly = 1) { TagManager.logInteraction(sut.tagPath, "favorite-added") }
    assertEquals(listOf(visuals), snackbars)
    assertEquals(listOf<MovieDetailsIntent>(MovieDetailsIntent.DismissFavoriteAdded), intents)
}
```

## Effect Composable: visuals content

Use `ComposeTestRule` with Robolectric and assert the `UiSnackbarVisuals` received by the callback. Read the expected texts from the resources, never from literals.

### Example: `MediaFavoriteAddedEffectTest.kt`

```kotlin
@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class MediaFavoriteAddedEffectTest {

    @get:Rule
    val rule = createComposeRule()

    private val context = RuntimeEnvironment.getApplication()
    private val snackbars = mutableListOf<UiSnackbarVisuals>()

    @Test
    fun `MediaFavoriteAddedEffect should show the snackbar when the favorite is added`() {
        rule.setContent {
            MediaFavoriteAddedEffect(
                favoriteAdded = true,
                mediaTitle = "Interstellar",
                onShow = { snackbars.add(it) }
            )
        }
        rule.waitForIdle()

        assertEquals(1, snackbars.size)
        val visuals = snackbars.first()
        assertTrue(visuals is UiSnackbarVisuals.Close)
        assertEquals(context.getString(R.string.favorite_added_title), visuals.title)
        assertEquals("Interstellar", visuals.message)
        assertEquals(UiIconSource.vector(Lucide.Heart), visuals.icon)
    }

    @Test
    fun `MediaFavoriteAddedEffect should not show the snackbar when no favorite is added`() {
        rule.setContent {
            MediaFavoriteAddedEffect(
                favoriteAdded = false,
                mediaTitle = "Interstellar",
                onShow = { snackbars.add(it) }
            )
        }
        rule.waitForIdle()

        assertTrue(snackbars.isEmpty())
    }
}
```

### Key Requirements:
- **Three Layers:** Test the ViewModel flag, the `Actions` method and the effect Composable separately.
- **Negative Cases:** Always test that the snackbar is **not** shown when the trigger does not apply (failure, opposite action, flag `false`).
- **Reset:** Always assert that the reset intent is sent after showing.
- **Idle:** Call `rule.waitForIdle()` before asserting, since the visuals are emitted from a `LaunchedEffect`.
- **Run:** `./gradlew :presentation:testDebugUnitTest`.
