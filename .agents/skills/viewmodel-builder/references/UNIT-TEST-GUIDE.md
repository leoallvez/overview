# ViewModel Unit Test Guide

This reference provides the standard pattern for creating unit tests for ViewModels in the **Overview** project using **MockK** and **Kotlin Coroutines Test**.

## Testing Overview

ViewModel unit tests verify state transitions, interaction with UseCases, and correct dispatcher usage.

### Example: `AppleDetailsViewModelTest.kt`

```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class AppleDetailsViewModelTest {

    private val useCase = mockk<IGetAppleByIdUseCase>()
    private val testDispatcher = StandardTestDispatcher()
    
    private lateinit var viewModel: AppleDetailsViewModel

    @Before
    fun setup() {
        viewModel = AppleDetailsViewModel(useCase, testDispatcher)
    }

    @Test
    fun `when load intent is received, should update state to success`() = runTest(testDispatcher) {
        // Given
        val apple = Apple(id = 1, description = "Test")
        coEvery { useCase(1) } returns UseCaseState.Success(apple)

        // When
        viewModel.handleIntent(AppleIntent.Load(1))
        
        // Advance time to allow coroutine execution
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is UiState.Success)
        assertEquals("Test", (state as UiState.Success).data?.description)
    }
    
    @Test
    fun `when use case fails, should update state to error`() = runTest(testDispatcher) {
        // Given
        coEvery { useCase(1) } returns UseCaseState.Error(Exception("Failed"))

        // When
        viewModel.handleIntent(AppleIntent.Load(1))
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is UiState.Error)
    }
}
```

### Key Requirements:

- **Rule:** Use `StandardTestDispatcher` and `runTest` to control coroutine timing.
- **Mocking:** Use `MockK` (specifically `mockk<T>()` and `coEvery`) for UseCase dependencies.
- **Assertions:** Verify the final `uiState.value` after calling `advanceUntilIdle()`.
- **Intents:** Trigger logic exclusively through `handleIntent`.
- **Coverage:** Test all state transitions (Loading -> Success/Error) for each Intent.
