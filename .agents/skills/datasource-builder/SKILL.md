---
name: datasource-builder
description: It builds a new Data Source (Remote or Local) in the :data module. Use it when you need to interact with APIs (Retrofit) or Local Database (Room/DataStore).
metadata:
  author: Singular
  version: "1.0"
  keywords:
  - data
  - datasource
  - remote
  - local
  - retrofit
  - room
---

## Core Workflow

- [ ] Step 1: Identify the type of Data Source (Remote or Local) and its dependencies (ApiService or Dao). Ask the user for these details.
- [ ] Step 2: Create a plan detailing the interface and implementation. Present it for approval before moving to the next steps.
- [ ] Step 3: Define the interface in the appropriate package:
    - **Remote:** `br.dev.singular.overview.data.network.source`.
    - **Local:** `br.dev.singular.overview.data.local.source`.
    - The interface should start with `I` (e.g., `IMediaRemoteDataSource`).
- [ ] Step 4: Implement the Data Source class in the same package.
- [ ] Step 5: Use `@Inject constructor` for dependencies.
- [ ] Step 6: For **Remote** Data Sources:
    - Use `ApiService` methods.
    - Wrap responses using `responseToResult(response)` to return `DataResult<T>`.
- [ ] Step 7: For **Local** Data Sources:
    - Use `Dao` methods.
    - Implement pagination logic using `BuildConfig.PAGE_SIZE` if applicable.
- [ ] Step 8: Create a unit test for the Data Source in `data/src/test/java/...` using MockK and Kluent.
- [ ] Step 9: All new tests must pass successfully.
- [ ] Step 10: Register the Data Source in the DI module using the [hilt-integrator](../hilt-integrator/SKILL.md) skill.
- [ ] Step 11: Standardize the commit message using the [git-standardizer](../git-standardizer/SKILL.md) skill.

## Architecture Guidelines

Data Sources are the lowest level of the data layer, responsible for raw data retrieval.

- **Isolation:** Data Sources should only know about their specific technology (Retrofit for Remote, Room for Local).
- **Result Wrapping:** Remote data sources MUST use `DataResult` to handle network states.
- **Dependency Injection:** Always use `@Inject` for Hilt support.
- **Pagination:** Local Data Sources should handle offset/limit calculations based on `BuildConfig.PAGE_SIZE`.

## Code Examples

### Remote Data Source
```kotlin
class AppleRemoteDataSource @Inject constructor(
    private val api: ApiService
) : IAppleRemoteDataSource {
    override suspend fun getById(id: Long) = responseToResult(api.getAppleById(id))
}
```

### Local Data Source
```kotlin
class AppleLocalDataSource @Inject constructor(
    private val dao: AppleDao
) : IAppleLocalDataSource {
    override suspend fun getById(id: Long) = dao.getById(id)
}
```

## References

For detailed patterns, code examples, and testing strategies, refer to:

- [Data Source Unit Test Guide](references/UNIT-TEST-GUIDE.md): Testing raw data retrieval and error states.

## Mandatory Rules

- **Location:** MUST be in the `:data` module.
- **Naming:** Interfaces MUST start with `I` prefix.
- **Remote Wrapper:** MUST use `responseToResult` for all API calls.
- **Plan Approval:** You MUST NOT modify files until the user approves the implementation plan in Step 2.
- **Tests Success:** All new tests MUST pass before completing the task.
