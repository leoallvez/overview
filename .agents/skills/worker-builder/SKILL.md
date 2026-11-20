---
name: worker-builder
description: It builds a new Background Worker using WorkManager and Hilt. Use it to implement scheduled or asynchronous tasks (like cache cleanup or data sync) that should run independently of the UI.
metadata:
  author: Singular
  version: "1.1"
  keywords:
  - workmanager
  - worker
  - hiltworker
  - background-task
  - data
---

## Core Workflow

- [ ] Step 1: Identify the task's purpose and the Use Case it will execute. Refer to
  the [usecase-builder](../usecase-builder/SKILL.md) skill for the domain logic. Ask the user for
  these details.
- [ ] Step 2: Create a plan detailing the worker implementation and its dependencies. Present it for
  approval.
- [ ] Step 3: Create the worker class in the `:data` module under
  `br.dev.singular.overview.data.local.workers`.
- [ ] Step 4: Annotate the class with `@HiltWorker`.
- [ ] Step 5: Use `@AssistedInject constructor` to inject `Context`, `WorkerParameters`, and
  required Use Cases (using qualifiers if needed).
- [ ] Step 6: Extend `UseCaseWorker` to benefit from the standardized result handling.
- [ ] Step 7: Implement `suspend fun runWork()` by calling the corresponding Use Case.
- [ ] Step 8: Register the worker in `CacheWorkScheduler.schedule()` (see
  [Scheduling](#scheduling)).
- [ ] Step 9: Create a unit test for the worker in `data/src/test/java/...` verifying both success
  and failure scenarios, and update `CacheWorkSchedulerTest` with the new unique work name, policy
  and constraints.
- [ ] Step 10: All new tests must pass successfully.
- [ ] Step 11: Standardize the commit message using
  the [git-standardizer](../git-standardizer/SKILL.md) skill.

## Architecture Guidelines

Workers handle background operations and should be as thin as possible, delegating logic to Use
Cases.

- **Standardization:** Always extend `UseCaseWorker`. It automatically maps `UseCaseState.Success`
  to `Result.success()` and `UseCaseState.Failure` to `Result.failure()`, except for failures caused
  by an `IOException`, which return `Result.retry()` (with WorkManager's exponential backoff) up to
  5 retries.
- **Hilt Integration:** Use `@HiltWorker` and `@AssistedInject`. Dependencies (like Use Cases) must
  be parameters of the constructor.
- **Error Logging:** `UseCaseWorker` handles logging via Timber. Do not add redundant logs unless
  specific context is needed.

## Scheduling

Workers are scheduled by `CacheWorkScheduler` (`br.dev.singular.overview.data.local.workers`), a
Hilt `@Singleton` that receives `WorkManager` by injection and is triggered once by
`CustomApplication.onCreate()` through `schedule()`.

- **One-time vs. periodic:** Use `makeOneTime<T>()` for tasks that run on every app start and
  `makePeriodic<T>()` for recurring tasks.
- **Unique work name:** Always pass an explicit `uniqueWorkName` in snake_case with the `_work`
  suffix (e.g., `refresh_catalog_cache_work`). Never derive it from the class name, since R8
  obfuscation changes it between builds and breaks deduplication.
- **Policies:** One-time work uses `ExistingWorkPolicy.KEEP`. Periodic work uses
  `ExistingPeriodicWorkPolicy.UPDATE`, so changes to the interval or constraints reach existing
  installs.
- **Constraints:** Pass `networkConstraints` when the Use Case reads from the network. Periodic work
  always uses it.

```kotlin
fun schedule() {
    makeOneTime<MyFeatureCacheWorker>(uniqueWorkName = "my_feature_cache_work")
    makeOneTime<MyFeatureSyncWorker>(
        uniqueWorkName = "my_feature_sync_work",
        constraints = networkConstraints
    )
    makePeriodic<MyFeatureRefreshWorker>(
        uniqueWorkName = "my_feature_refresh_work",
        repeatIntervalHours = 24L
    )
}
```

## Code Example

```kotlin
@HiltWorker
class MyFeatureCacheWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val useCase: IMyFeatureUseCase
) : UseCaseWorker<Unit>(context, params) {

    override suspend fun runWork() = useCase()
}
```

## References

For detailed patterns and testing strategies, refer to:

- [Worker Unit Test Guide](references/UNIT-TEST-GUIDE.md): Testing background tasks, result
  mapping and scheduling.

## Mandatory Rules

- **Location:** Workers MUST be created in the `:data` module under
  `br.dev.singular.overview.data.local.workers`.
- **Base Class:** Every worker MUST extend `UseCaseWorker`.
- **DI Pattern:** MUST use `@HiltWorker` and `@AssistedInject`.
- **Scheduling:** Every worker MUST be scheduled through `CacheWorkScheduler` with an explicit
  unique work name. Never call `WorkManager` directly elsewhere.
- **Periodic Policy:** Periodic work MUST use `ExistingPeriodicWorkPolicy.UPDATE`.
- **Plan Approval:** You MUST NOT modify files until the user approves the implementation plan in
  Step 2.
- **Tests Success:** All new tests MUST pass before completing the task.
