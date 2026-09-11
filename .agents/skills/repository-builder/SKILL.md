---
name: repository-builder
description: It builds a new Repository implementation in the :data module. Use it when you need to coordinate between local and remote data sources and map them to domain entities.
metadata:
  author: Singular
  version: "1.0"
  keywords:
  - data
  - repository
  - mapping
  - persistence
  - remote
  - local
---

## Core Workflow

- [ ] Step 1: Identify the domain interface(s) the repository will implement and the required data sources (remote/local). Refer to the [datasource-builder](../datasource-builder/SKILL.md) skill for data source details. Ask the user for these details.
- [ ] Step 2: Create a plan detailing the repository implementation, dependencies, and mapping strategy. Present it for approval before moving to the next steps.
- [ ] Step 3: Ensure the domain interface exists in `:domain/repository` and the necessary data sources are ready in `:data`.
- [ ] Step 4: Create the implementation class in `:data/src/main/java/br/dev/singular/overview/data/repository/<feature_name>`.
- [ ] Step 5: Use `@Inject constructor` for dependencies. Use `toDomain()` and `toData()` mappers following the [mapper-builder](../mapper-builder/SKILL.md) skill.
- [ ] Step 6: Implement the interface methods, coordinating between data sources and handling result states (e.g., `DataResult`).
- [ ] Step 7: Create a unit test for the repository in `:data/src/test/java/...` using MockK and Kluent.
- [ ] Step 8: All new tests must pass successfully.
- [ ] Step 9: Register the Repository in the DI module using the [hilt-integrator](../hilt-integrator/SKILL.md) skill if it's a new implementation.
- [ ] Step 10: Standardize the commit message using the [git-standardizer](../git-standardizer/SKILL.md) skill.

## Architecture Guidelines

Repositories are the implementations of domain contracts, hiding data complexity from the business logic.

- **Dependency Injection:** Use Hilt's `@Inject` for all dependencies.
- **Coordination:** A single repository can coordinate multiple data sources (e.g., API + Room).
- **Mapping:** Data models from API/Database MUST be mapped to domain entities before returning. Mappers should reside in `util/mappers`.
- **Parallelism:** Use `coroutineScope` and `async` when fetching independent data from multiple sources.
- **Result Handling:** Handle `DataResult.Error` gracefully, usually returning `null` or an empty list depending on the contract.

## Code Example

```kotlin
class AppleRepository @Inject constructor(
    private val remoteDataSource: IAppleRemoteDataSource,
    private val localDataSource: IAppleLocalDataSource
) : GetById<Apple> {

    override suspend fun getById(id: Long): Apple? {
        return when (val response = remoteDataSource.getById(id)) {
            is DataResult.Success -> {
                val domain = response.data.toDomain()
                localDataSource.save(domain.toData())
                domain
            }
            is DataResult.Error -> localDataSource.getById(id)?.toDomain()
        }
    }
}
```

## References

For detailed patterns, code examples, and testing strategies, refer to:

- [Repository Unit Test Guide](references/UNIT-TEST-GUIDE.md): Testing data coordination and mapping logic.

## Mandatory Rules

- **Location:** Implementation MUST be in the `:data` module.
- **Naming:** Follow the PascalCase naming convention (e.g., `AppleRepository`).
- **Mappers:** Always use mappers to bridge data and domain models. DO NOT leak data models into the `:domain` module.
- **Hilt:** Always use constructor injection with `@Inject`.
- **Plan Approval:** You MUST NOT modify files until the user approves the implementation plan in Step 2.
- **Tests Success:** All new tests MUST pass before completing the task.
