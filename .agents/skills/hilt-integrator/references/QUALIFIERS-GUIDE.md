# Hilt Qualifiers Guide

This guide explains how to use qualifiers in the **Overview** project to resolve dependency ambiguity.

## When to Use Qualifiers

Use qualifiers when you have:
1.  Multiple implementations of the same interface.
2.  The same interface used in different contexts (e.g., a generic `IDeleteUseCase` for different entities).

## 1. Define the Qualifier

Qualifiers are annotations located in the `di` package of the corresponding module (app, data, or presentation).

```kotlin
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DeleteApple
```

## 2. Use in Module (@Provides or @Binds)

Apply the qualifier to the provider or binder method.

```kotlin
@Singleton
@Provides
@DeleteApple
fun provideDeleteAppleUseCase(
    repo: AppleRepository
): IDeleteUseCase {
    return DeleteAppleUseCase(deleter = repo)
}
```

## 3. Use in Injection Site

Apply the same qualifier where the dependency is injected (e.g., in a ViewModel or another UseCase).

```kotlin
class AppleViewModel @Inject constructor(
    @DeleteApple private val deleteUseCase: IDeleteUseCase
) : ViewModel()
```

## Existing Qualifiers in the Project

Check these files for examples:
- `br.dev.singular.overview.data.di.DeleteQualifiers.kt`
- `br.dev.singular.overview.presentation.di.domain.UseCaseQualifiers.kt`
