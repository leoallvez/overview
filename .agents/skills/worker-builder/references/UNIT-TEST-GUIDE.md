# Worker Unit Test Guide

This reference provides the standard pattern for creating unit tests for Workers in the **Overview**
project.

## Testing Overview

Worker unit tests verify that the `doWork()` method (inherited from `UseCaseWorker`) correctly maps
the Use Case results to WorkManager's `Result.success()` or `Result.failure()`.

Scheduling is covered by `CacheWorkSchedulerTest`, which must be updated whenever a worker is added
to `CacheWorkScheduler` (see [Scheduler Tests](#scheduler-tests)).

### Example: `MyFeatureCacheWorkerTest.kt`

```kotlin
class MyFeatureCacheWorkerTest {

    private val context: Context = mockk(relaxed = true)
    private val params: WorkerParameters = mockk(relaxed = true)
    private val useCase: IMyFeatureUseCase = mockk()

    @Test
    fun `doWork should return Success when useCase returns Success`() = runTest {
        // arrange
        coEvery { useCase() } returns UseCaseState.Success(Unit)
        val sut = MyFeatureCacheWorker(context, params, useCase)

        // act
        val result = sut.doWork()

        // assert
        assertEquals(ListenableWorker.Result.success(), result)
    }

    @Test
    fun `doWork should return Failure when useCase returns Failure`() = runTest {
        // arrange
        coEvery { useCase() } returns UseCaseState.Failure(FailType.Invalid)
        val sut = MyFeatureCacheWorker(context, params, useCase)

        // act
        val result = sut.doWork()

        // assert
        assertEquals(ListenableWorker.Result.failure(), result)
    }
}
```

### Key Requirements:

- **Mocking Context & Params:** Use `mockk(relaxed = true)` for `Context` and `WorkerParameters` as
  they are usually not directly used in `runWork()`.
- **SUT Naming:** Use `sut` for the worker implementation.
- **Coroutines:** Use `runTest` to wrap the test execution.
- **Result Assertion:** Assert against `ListenableWorker.Result.success()` or
  `ListenableWorker.Result.failure()`.
- **Location:** Place tests in `:data/src/test/java/...` mirroring the package structure.
- **Markers:** Use the `// arrange`, `// act`, `// assert` markers.

## Scheduler Tests

`CacheWorkSchedulerTest` mocks `WorkManager` and verifies that `schedule()` enqueues each worker with
its unique work name and policy. Workers that require network must also have their constraint
asserted by capturing the request.

### Example: registering `MyFeatureSyncWorker`

```kotlin
class CacheWorkSchedulerTest {

    private val workManager: WorkManager = mockk(relaxed = true)
    private val sut = CacheWorkScheduler(workManager)

    @Test
    fun `schedule should enqueue my feature sync worker`() {
        // arrange
        // act
        sut.schedule()

        // assert
        verify {
            workManager.enqueueUniqueWork(
                "my_feature_sync_work",
                ExistingWorkPolicy.KEEP,
                any<OneTimeWorkRequest>()
            )
        }
    }

    @Test
    fun `schedule should require network connection for my feature sync worker`() {
        // arrange
        val request = slot<OneTimeWorkRequest>()

        // act
        sut.schedule()

        // assert
        verify {
            workManager.enqueueUniqueWork(
                "my_feature_sync_work",
                ExistingWorkPolicy.KEEP,
                capture(request)
            )
        }
        assertEquals(
            NetworkType.CONNECTED,
            request.captured.workSpec.constraints.requiredNetworkType
        )
    }
}
```

### Key Requirements:

- **Unique Name & Policy:** Verify the exact `uniqueWorkName` and policy (`ExistingWorkPolicy.KEEP`
  for one-time, `ExistingPeriodicWorkPolicy.UPDATE` for periodic).
- **Request Type:** Match `any<OneTimeWorkRequest>()` or `any<PeriodicWorkRequest>()` accordingly.
- **Constraints:** Use `slot` + `capture` to assert `requiredNetworkType` when the worker needs
  network.
