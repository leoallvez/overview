---
name: hilt-integrator
description: It handles dependency injection configuration using Hilt. Use it when you need to register new DataSources, Repositories, or UseCases in the DI modules.
metadata:
  author: Singular
  version: "1.0"
  keywords:
  - hilt
  - di
  - dependency-injection
  - provides
  - binds
  - module
---

## Core Workflow

- [ ] Step 1: Identify what needs to be registered (DataSource, Repository, or UseCase) and which Hilt Module is appropriate. Refer to [datasource-builder](../datasource-builder/SKILL.md), [repository-builder](../repository-builder/SKILL.md), or [usecase-builder](../usecase-builder/SKILL.md) for details. Ask the user for these details.
- [ ] Step 2: Create a plan detailing the changes in the Hilt modules, including any new qualifiers if needed. Present it for approval.
- [ ] Step 3: For **DataSources**:
    - If it's a **Local** DataSource, update `LocalDataSourceModule.kt` using `@Binds`.
    - If it's a **Remote** DataSource, update `RemoteDataSourceModule.kt` using `@Binds`.
- [ ] Step 4: For **UseCases**:
    - Update `UseCaseModule.kt` using `@Provides`.
    - Ensure the UseCase is provided as an interface and receives its dependencies (usually concrete Repositories) in the provider method.
- [ ] Step 5: If the same interface has multiple implementations or usage contexts, create/use a **Qualifier** (Annotation) located in the appropriate `di` package.
- [ ] Step 6: Verify the Hilt configuration by running a Gradle build or sync to ensure there are no missing dependencies or circular references.
- [ ] Step 7: Standardize the commit message using the [git-standardizer](../git-standardizer/SKILL.md) skill.

## Architecture Guidelines

Dependency Injection is managed centrally in the `:app` module to glue all other modules together.

- **Module Location:** DI modules are organized by layer under `br.dev.singular.overview.di`.
- **Binds vs Provides:**
    - Use `@Binds` in abstract classes (like `LocalDataSourceModule`) for simple interface-to-implementation mapping where the implementation has an `@Inject` constructor.
    - Use `@Provides` in standard classes (like `UseCaseModule`) when manual instantiation or complex configuration is required.
- **Singleton Scope:** Most DataSources and UseCases should be annotated with `@Singleton`.
- **Qualifiers:** Use qualifiers to resolve ambiguity when multiple bindings exist for the same type.

## Code Examples

### @Binds for Data Source
```kotlin
@Binds
abstract fun bindAppleRemoteDataSource(
    source: AppleRemoteDataSource
): IAppleRemoteDataSource
```

### @Provides for Use Case
```kotlin
@Singleton
@Provides
fun provideGetAppleByIdUseCase(
    repo: AppleRepository
): IGetAppleByIdUseCase {
    return GetAppleByIdUseCase(getter = repo)
}
```

## References

For detailed patterns and qualifier usage, refer to:

- [Hilt Qualifiers Guide](references/QUALIFIERS-GUIDE.md): Handling dependency ambiguity.

## Mandatory Rules

- **Location:** DI modules MUST reside in the `:app" module under `br.dev.singular.overview.di`.
- **Interface Driven:** Always bind/provide interfaces, not concrete implementations (except for repositories passed as dependencies).
- **Plan Approval:** You MUST NOT modify files until the user approves the DI integration plan in Step 2.
- **Build Verification:** Ensure the project compiles after DI changes.
