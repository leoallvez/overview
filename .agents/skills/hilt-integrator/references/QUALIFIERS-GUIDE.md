# Hilt Qualifiers Guide

This guide explains how to use qualifiers in the **Overview** project to resolve dependency ambiguity.

## When to Use Qualifiers

Use qualifiers when you have:
1.  Multiple implementations of the same interface.
2.  The same interface used in different contexts (e.g., `IGetRemoteMediasUseCase` for discover and search).

> Prefer a named interface in `:domain` when each implementation has its own meaning
> (e.g., `IDeleteMediasUseCase : IDeleteUseCase`). Reserve qualifiers for cases where the
> consumer really depends on the same contract.

## 1. Define the Qualifier

Qualifiers are annotations located in the `di` package of the corresponding module (app, data, or presentation).

```kotlin
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DiscoverApple
```

## 2. Use in Module (@Provides or @Binds)

Apply the qualifier to the provider or binder method.

```kotlin
@Singleton
@Provides
@DiscoverApple
fun provideDiscoverAppleUseCase(
    repo: AppleRepository
): IGetAppleUseCase {
    return DiscoverAppleUseCase(getter = repo)
}
```

## 3. Use in Injection Site

Apply the same qualifier where the dependency is injected (e.g., in a ViewModel or another UseCase).

```kotlin
class AppleViewModel @Inject constructor(
    @param:DiscoverApple private val useCase: IGetAppleUseCase
) : ViewModel()
```

## Existing Qualifiers in the Project

Check this file for examples:
- `br.dev.singular.overview.presentation.di.domain.MediaUseCaseQualifiers.kt`
