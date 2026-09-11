# Worker Unit Test Guide

This reference provides the standard pattern for creating unit tests for Workers in the **Overview** project.

## Testing Overview

Worker unit tests verify that the `doWork()` method (inherited from `UseCaseWorker`) correctly maps the Use Case results to WorkManager's `Result.success()` or `Result.failure()`.

### Example: `MyFeatureCacheWorkerTest.kt`

```kotlin
class MyFeatureCacheWorkerTest {

    private val context: Context = mockk(relaxed = true)
    private val params: WorkerParameters = mockk(relaxed = true)
    private val useCase: IMyFeatureUseCase = mockk()

    @Test
    fun `doWork should return Success when useCase returns Success`() = runTest {
        // Given
        coEvery { useCase() } returns UseCaseState.Success(Unit)
        val sut = MyFeatureCacheWorker(context, params, useCase)

        // When
        val result = sut.doWork()

        // Then
        assertEquals(ListenableWorker.Result.success(), result)
    }

    @Test
    fun `doWork should return Failure when useCase returns Failure`() = runTest {
        // Given
        coEvery { useCase() } returns UseCaseState.Failure(FailType.Invalid)
        val sut = MyFeatureCacheWorker(context, params, useCase)

        // When
        val result = sut.doWork()

        // Then
        assertEquals(ListenableWorker.Result.failure(), result)
    }
}
```

### Key Requirements:

- **Mocking Context & Params:** Use `mockk(relaxed = true)` for `Context` and `WorkerParameters` as they are usually not directly used in `runWork()`.
- **SUT Naming:** Use `sut` for the worker implementation.
- **Coroutines:** Use `runTest` to wrap the test execution.
- **Result Assertion:** Assert against `ListenableWorker.Result.success()` or `ListenableWorker.Result.failure()`.
- **Location:** Place tests in `:data/src/test/java/...` mirroring the package structure.
