---
name: usecase-builder
description: It builds a new Use Case following the project's architecture in the :domain module. Use it when you need to implement business logic that coordinates repositories.
metadata:
  author: Singular
  version: "1.0"
  keywords:
  - domain
  - usecase
  - business-logic
  - pure-kotlin
  - clean-architecture
---

## Core Workflow

- [ ] Step 1: Identify the Use Case name, purpose, and the repository interface(s) it will depend on. Refer to the [repository-builder](../repository-builder/SKILL.md) skill for repository details. Ask the user for these details.
- [ ] Step 2: Create a plan detailing the Use Case interface and implementation. Present it for approval before moving to the next steps.
- [ ] Step 3: Define the interface in `br.dev.singular.overview.domain.usecase.<feature_name>`. The interface should start with `I` (e.g., `IGetAppleByIdUseCase`).
- [ ] Step 4: Implement the Use Case class in the same package (e.g., `GetAppleByIdUseCase`).
- [ ] Step 5: Implement the logic using `suspend operator fun invoke` for single actions. Use `runSafely` wrapper to return `UseCaseState<T>`.
- [ ] Step 6: Ensure the implementation is pure Kotlin/Java, residing in the `:domain` module with no Android dependencies.
- [ ] Step 7: Create a unit test for the Use Case in `:domain/src/test/java/...` using MockK.
- [ ] Step 8: All new tests must pass successfully.
- [ ] Step 9: Register the Use Case in the DI module using the [hilt-integrator](../hilt-integrator/SKILL.md) skill.
- [ ] Step 10: Standardize the commit message using the [git-standardizer](../git-standardizer/SKILL.md) skill.

## Architecture Guidelines

Use Cases represent a single piece of business logic and are the entry points to the Domain layer.

- **Dependency Inversion:** Use Cases must interact with repositories only through interfaces defined in the `:domain` module.
- **Single Responsibility:** Each Use Case should ideally do one thing. If it needs to perform multiple related operations (like Observe/Save), define specific methods in the interface.
- **Pure Domain:** No Android dependencies (Context, Bundle, etc.) are allowed in the `:domain` module.
- **Error Handling:** Use the `runSafely` utility to catch exceptions and return a `UseCaseState.Failure`.

## References

For detailed patterns, code examples, and testing strategies, refer to:

- [Unit Test Guide](references/UNIT-TEST-GUIDE.md): Testing business logic and repository coordination.

## Code Example

```kotlin
interface IGetAppleByIdUseCase {
    suspend operator fun invoke(id: Long): UseCaseState<Apple?>
}

class GetAppleByIdUseCase(
    private val getter: GetById<Apple>
) : IGetAppleByIdUseCase {
    override suspend fun invoke(id: Long) = runSafely { 
        getter.getById(id) 
    }
}
```

## Mandatory Rules

- **Location:** All files MUST be created in the `:domain` module.
- **Interface Naming:** Interfaces MUST start with `I` prefix.
- **Implementation Naming:** Implementation classes MUST NOT have a prefix or suffix (e.g., use `GetAppleByIdUseCase`, NOT `GetAppleByIdUseCaseImpl`).
- **Safety Wrapper:** Always use `runSafely` for operations that can fail (network, database).
- **Plan Approval:** You MUST NOT modify files until the user approves the implementation plan in Step 2.
- **Tests Success:** All new tests MUST pass before completing the task.
