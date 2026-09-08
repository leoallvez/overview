# Use Case Unit Test Guide

This reference provides the standard pattern for creating unit tests for Use Cases in the **Overview** project using **MockK** and **Kotlin Coroutines Test**.

## Testing Overview

Use Case unit tests verify business logic coordination, repository interactions, and correct result wrapping using `UseCaseState`.

### Example: `GetAppleByIdUseCaseTest.kt`

```kotlin
class GetAppleByIdUseCaseTest {

    private lateinit var sut: IGetAppleByIdUseCase
    private val getter = mockk<GetById<Apple>>()

    @Before
    fun setup() {
        sut = GetAppleByIdUseCase(getter)
    }

    @Test
    fun `invoke should return success when repository returns data`() = runTest {
        // Given
        val apple = Apple(id = 1, description = "Test")
        coEvery { getter.getById(1) } returns apple

        // When
        val result = sut.invoke(1)

        // Then
        coVerify(exactly = 1) { getter.getById(1) }
        assertEquals(UseCaseState.Success(apple), result)
    }

    @Test
    fun `invoke should return failure when repository throws exception`() = runTest {
        // Given
        val exception = RuntimeException("DB Error")
        coEvery { getter.getById(any()) } throws exception

        // When
        val result = sut.invoke(1)

        // Then
        assertTrue(result is UseCaseState.Failure)
        val failure = result as UseCaseState.Failure
        assertEquals(exception, (failure.type as FailType.Exception).throwable)
    }
}
```

### Key Requirements:

- **SUT Naming:** Use `sut` (System Under Test) for the Use Case implementation.
- **Mocking:** Use `MockK` for all repository dependencies. Prefer `coEvery` and `coVerify` for suspend functions.
- **Coroutines:** Use `runTest` to wrap the test execution. Since Use Cases in `:domain` don't inject dispatchers (they rely on the caller's context or `runSafely` implementation), you usually don't need a `StandardTestDispatcher` here.
- **Verification:** Always verify that the repository was called with the correct parameters using `coVerify`.
- **States:** Test at least two scenarios:
    1.  **Success Path:** Data returned correctly (including `null` if applicable).
    2.  **Failure Path:** Exception thrown by repository (verifying `runSafely` behavior).
- **Location:** Place tests in `:domain/src/test/java/...` mirroring the package structure of the implementation.
