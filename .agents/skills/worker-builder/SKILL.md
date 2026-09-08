---
name: worker-builder
description: It builds a new Background Worker using WorkManager and Hilt. Use it to implement scheduled or asynchronous tasks (like cache cleanup or data sync) that should run independently of the UI.
metadata:
  author: Singular
  version: "1.0"
  keywords:
  - workmanager
  - worker
  - hiltworker
  - background-task
  - data
---

## Core Workflow

- [ ] Step 1: Identify the task's purpose and the Use Case it will execute. Refer to the [usecase-builder](../usecase-builder/SKILL.md) skill for the domain logic. Ask the user for these details.
- [ ] Step 2: Create a plan detailing the worker implementation and its dependencies. Present it for approval.
- [ ] Step 3: Create the worker class in the `:data` module under `br.dev.singular.overview.data.local.workers`.
- [ ] Step 4: Annotate the class with `@HiltWorker`.
- [ ] Step 5: Use `@AssistedInject constructor` to inject `Context`, `WorkerParameters`, and required Use Cases (using qualifiers if needed).
- [ ] Step 6: Extend `UseCaseWorker` to benefit from the standardized result handling.
- [ ] Step 7: Implement `suspend fun runWork()` by calling the corresponding Use Case.
- [ ] Step 8: Create a unit test for the worker in `data/src/test/java/...` verifying both success and failure scenarios.
- [ ] Step 9: All new tests must pass successfully.
- [ ] Step 10: Standardize the commit message using the [git-standardizer](../git-standardizer/SKILL.md) skill.

## Architecture Guidelines

Workers handle background operations and should be as thin as possible, delegating logic to Use Cases.

- **Standardization:** Always extend `UseCaseWorker`. It automatically maps `UseCaseState.Success` to `Result.success()` and `UseCaseState.Failure` to `Result.failure()`.
- **Hilt Integration:** Use `@HiltWorker` and `@AssistedInject`. Dependencies (like Use Cases) must be parameters of the constructor.
- **Error Logging:** `UseCaseWorker` handles logging via Timber. Do not add redundant logs unless specific context is needed.

## Code Example

```kotlin
@HiltWorker
class MyFeatureCacheWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val useCase: IMyFeatureUseCase
) : UseCaseWorker(context, params) {

    override suspend fun runWork() = useCase()
}
```

## References

For detailed patterns and testing strategies, refer to:

- [Worker Unit Test Guide](references/UNIT-TEST-GUIDE.md): Testing background tasks and result mapping.

## Mandatory Rules

- **Location:** Workers MUST be created in the `:data` module under `br.dev.singular.overview.data.local.workers`.
- **Base Class:** Every worker MUST extend `UseCaseWorker`.
- **DI Pattern:** MUST use `@HiltWorker` and `@AssistedInject`.
- **Plan Approval:** You MUST NOT modify files until the user approves the implementation plan in Step 2.
- **Tests Success:** All new tests MUST pass before completing the task.
