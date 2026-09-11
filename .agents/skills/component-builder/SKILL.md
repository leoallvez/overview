---
name: component-builder
description: It builds a new Jetpack Compose component following a specific pattern. Use it when users want to create a component from scratch.
metadata:
  author: Singular
  version: "1.0"
  keywords:
  - ui 
  - kotlin
  - compose
  - component
  - design-system
---

## Core Workflow

- [ ] Step 1: Identify the component's name and purpose. Create a plan and present it for approval before moving to the next steps.
- [ ] Step 2: Create a new folder inside `br.dev.singular.overview.presentation.ui.components.<component_folder_name>`.
- [ ] Step 3: Create a Kotlin file named in PascalCase with the `Ui` prefix (e.g., `UiMediaCard.kt`).
- [ ] Step 4: Define a stateless Composable with `internal` visibility. Use `UiText` for all text and `dimensionResource(R.dimen.spacing_...)` for margins/padding.
- [ ] Step 5: For complex components, create a `Skeleton` variant (e.g., `UiMediaCardSkeleton`) using `UiShimmerBox`.
- [ ] Step 6: If the component has multiple interactions, define an `Actions` data class. For simple components, use standard lambda callbacks.
- [ ] Step 7: Create internal preview functions using `@UiComponentPreview`. Show multiple states (Default, Loading/Skeleton, Edge Cases).
- [ ] Step 8: Create a unit test file in the corresponding test directory to verify interaction logic using `ComposeTestRule`.
- [ ] Step 9: Create a snapshot test class extending `UiSnapshotTest` that calls `snapshot()` for each preview state and generates its images.
- [ ] Step 10: All new tests (unit and snapshot) should pass successfully.
- [ ] Step 11: Standardize the commit message using the [git-standardizer](../git-standardizer/SKILL.md) skill.

## UI Model Guidelines

UI Models serve as the data contract for components, ensuring they remain stateless and decoupled from domain logic.

- **Contract-Based:** Use a dedicated `data class` (e.g., `MediaUiModel`) to pass data to complex components.
- **Pre-formatted Data:** All data formatting (dates, currency) must happen before reaching the Composable. Use `UiText` to handle both hardcoded strings and XML resources.
- **Domain Decoupling:** Components must never depend on `Domain Entities`. They should only know about their specific `UI Model`.

## References

For detailed patterns, code examples, and testing strategies, refer to:

- [Component Build Guide](references/BUILD-GUIDE.md): Standard structure and Skeleton implementation.
- [Unit Test Guide](references/UNIT-TEST-GUIDE.md): Testing logic and interactions.
- [Snapshot Test Guide](references/SNAPSHOT-TEST-GUIDE.md): Visual regression testing with Paparazzi.

## Mandatory rules

- **Internal Visibility:** All UI components should be `internal` unless they are designed for cross-module use.
- **Modifier & Semantics:** The first optional parameter MUST be `Modifier`. Use `.semantics(mergeDescendants = true) {}` on container layouts for better accessibility.
- **Design System Tokens:** NEVER use hardcoded DP/SP. Use `R.dimen` spacing tokens and project theme colors (e.g., `HighlightColor`, `WarningColor`).
- **Style Wrappers:** Use existing style classes like `UiBorderStyle`, `UiImageStyle`, or `UiIconStyle` instead of raw parameters when applicable.
- **KDoc Documentation:** Every component MUST have a KDoc block describing the purpose and all parameters using the `@param` tag.
- **Preview Independence:** Previews should be self-contained. Use helper composables with `remember` state to show interactive behavior (like toggle states).
- **UI Model Usage:** For complex components, always use a `UI Model` data class to manage data input. This facilitates testing and UI previews.
