# Overview Project - AI Agent Guide

This document provides context and guidelines for AI agents working on the **Overview** project.

## AI Agent Skills

To maintain consistency and follow the project's high standards, always use the specialized skills 
located in the `.agents/skills/` directory.

### Mandatory Workflow:
1.  **Identify:** Before starting any task, check if there is a relevant skill (e.g., `screen-builder`, `usecase-builder`).
2.  **Activate:** Read the skill's `SKILL.md` file to load the specific instructions and mandatory rules.
3.  **Plan:** Create an implementation plan and present it to the user for approval.
4.  **Execute:** Apply changes strictly following the loaded instructions and reference guides.
5.  **Verify:** Run all relevant tests (see [Testing Strategy](#testing-strategy)). A task is only considered complete if all tests pass.
6.  **Commit:** Use the `git-standardizer` skill to propose the commit message.

## Project Overview

**Overview** is an Android application designed to aggregate and navigate content across various
streaming services.
Users can search for media, filter by genre or type, and manage a collection of favorites.

## Architecture & Module Structure

The project follows **Clean Architecture** principles and is organized into a **multi-module**
structure.

### Module Hierarchy:

```text
overview/
├── app/                         → Entry point, DI configuration & Navigation.
├── domain/                      → Core Business Logic (Pure Kotlin/Java).
├── data/                        → Data Layer Implementation (Room, Retrofit).
└── presentation/                → UI Layer (Jetpack Compose, ViewModel, M3).
```

## Development Standards

To keep the codebase clean and maintainable, follow these standards:

### Naming Conventions:
- **ViewModels:** Use the `ViewModel` suffix (e.g., `HomeViewModel`).
- **Screens:** Use the `Screen` suffix for the main composable (e.g., `HomeScreen`).
- **Use Cases:** Use the `UseCase` suffix (e.g., `GetMediaListUseCase`).
- **Repositories:** In `:domain`, use the generic action-based interfaces from `Generics.kt` (e.g., `GetAll<T>`, `GetById<T>`). In `:data`, implement these using the `Repository` suffix, typically including the data source in the name (e.g., `CatalogRepository`, `MediaRemoteRepository`, `MediaLocalRepository`). No `Impl` suffix is used.
- **Mappers:** Use the `Mapper` suffix or extension functions named `toDomain()` / `toData()`.

### Testing Strategy:
- **Unit Tests:** Located in `src/test`. Mandatory for `UseCases`, `ViewModels` (logic), and `Mappers`.
- **Instrumentation Tests:** Located in `src/androidTest`. Used for `Room` databases and critical UI flows.
- **Module Responsibilities:**
    - `:domain`: Unit tests for business logic and UseCases.
    - `:data`: Unit tests for mappers and Repositories; Instrumentation tests for Local DataSources.
    - `:presentation`: Unit tests for ViewModels (State transitions); UI tests for Screens.

### Code Quality:
- **Formatting:** Follow the standard Kotlin style guide.
- **Immutability:** Favor `val` over `var` and use immutable data classes for UI state.
- **State:** Always use `StateFlow` to expose state from ViewModels.

## App module

This module is the **pure entry point** of the project. It acts as the "glue" using Hilt to satisfy 
dependencies across modules and manages navigation.

> **Important:** This module contains **no UI logic or business implementations**. 
> Refer to the [`feature-integrator`](.agents/skills/feature-integrator/SKILL.md) and [`hilt-integrator`](.agents/skills/hilt-integrator/SKILL.md) skills for integration details.

## Domain module

This module contains the core business logic. It must be **pure Kotlin/Java** with no Android dependencies.

- **Models:** Data classes representing business entities.
- **Use Cases:** Coordinate logic and interact with repository interfaces.
- **Repositories:** Generic interfaces defining data contracts.

> For implementation details, refer to the [`usecase-builder`](.agents/skills/usecase-builder/SKILL.md) skill.

## Data module

This module implements the repository interfaces, coordinating data between local and remote sources.

- **Data Sources:** Raw data access via Retrofit (Remote) or Room (Local).
- **Mappers:** Transform Data Models into Domain Entities and vice-versa.
- **Workers:** Handle background tasks using WorkManager.

> For implementation details, refer to the [`repository-builder`](.agents/skills/repository-builder/SKILL.md), [`datasource-builder`](.agents/skills/datasource-builder/SKILL.md), [`mapper-builder`](.agents/skills/mapper-builder/SKILL.md) and [`worker-builder`](.agents/skills/worker-builder/SKILL.md) skills.

## Presentation module

This module contains the UI layer built entirely with **Jetpack Compose** (Material 3).

- **Screens:** Orchestrate UI state and user interactions via UDF.
- **ViewModels:** Manage state using `UiState<T>` and handle intents.
- **Components:** Reusable UI atoms and molecules.
- **Tagging:** Manages analytics tracking.

> For implementation details, refer to the [`screen-builder`](.agents/skills/screen-builder/SKILL.md), [`viewmodel-builder`](.agents/skills/viewmodel-builder/SKILL.md), [`component-builder`](.agents/skills/component-builder/SKILL.md) and [`analytics-tagger`](.agents/skills/analytics-tagger/SKILL.md) skills.

## Skills & Reference Guides

For detailed examples, patterns, and testing strategies, always refer to the corresponding skill:

- **UI Components:** [`component-builder`](.agents/skills/component-builder/SKILL.md)
- **UI Screens:** [`screen-builder`](.agents/skills/screen-builder/SKILL.md)
- **ViewModels:** [`viewmodel-builder`](.agents/skills/viewmodel-builder/SKILL.md)
- **Business Logic:** [`usecase-builder`](.agents/skills/usecase-builder/SKILL.md)
- **Data Persistence:** [`repository-builder`](.agents/skills/repository-builder/SKILL.md) & [`datasource-builder`](.agents/skills/datasource-builder/SKILL.md)
- **DI & Integration:** [`hilt-integrator`](.agents/skills/hilt-integrator/SKILL.md) & [`feature-integrator`](.agents/skills/feature-integrator/SKILL.md)
- **Data Mapping:** [`mapper-builder`](.agents/skills/mapper-builder/SKILL.md)
- **Background Tasks:** [`worker-builder`](.agents/skills/worker-builder/SKILL.md)
- **Analytics:** [`analytics-tagger`](.agents/skills/analytics-tagger/SKILL.md)
- **Git Standards:** [`git-standardizer`](.agents/skills/git-standardizer/SKILL.md)

## Examples of Success

To maintain high quality, follow these examples when planning and executing tasks:

### Implementation Plan Example
> [!TIP]
> A good plan is specific about files and follows the order of dependencies.

```markdown
# Add Favorite Button to Media Detail

Implement a toggle favorite button in the media detail screen, persisting the state in the local database.

## Proposed Changes
### :domain
#### [MODIFY] [Generics.kt](file:///.../domain/repository/Generics.kt)
- Add generic interface if needed (e.g., `interface ToggleFavorite<T>`).

### :data
#### [MODIFY] [MediaLocalRepository.kt](file:///.../data/repository/media/MediaLocalRepository.kt)
- Implement `ToggleFavorite<Media>` using `LocalDataSource`.
#### [MODIFY] [LocalDataSource.kt](file:///.../data/local/LocalDataSource.kt)
- Add Room DAO call to update favorite status.

### :presentation
#### [MODIFY] [MediaDetailViewModel.kt](file:///.../presentation/ui/MediaDetailViewModel.kt)
- Add `FavoriteIntent` handling and update `UiState`.
```

### Commit Message Example
> [!NOTE]
> Use the `git-standardizer` skill for final formatting.

- `feat(presentation): add favorite toggle to media detail screen`
- `fix(data): resolve concurrency issue in local database update`
- `docs: update AGENTS.md with new success examples`
