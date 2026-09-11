---
name: mapper-builder
description: It builds data transformation mappers (Extension Functions) between layers (Data, Domain, UI). Use it to maintain clean architecture boundaries and ensure data is formatted correctly before reaching the UI.
metadata:
  author: Singular
  version: "1.0"
  keywords:
  - mapper
  - extension-function
  - data-transformation
  - domain
  - ui-model
  - data-model
---

## Core Workflow

- [ ] Step 1: Identify the source and target models. Ask the user for the direction (e.g., Data -> Domain, Domain -> UI, or Domain -> Data).
- [ ] Step 2: Create a plan detailing the mapper's location, the fields to be transformed, and any special formatting required. Present it for approval.
- [ ] Step 3: Create or update the mapper file using **Extension Functions**:
    - **Data -> Domain:** Create in `:data` module under `br.dev.singular.overview.data.util.mappers.dataToDomain`.
    - **Domain -> Data:** Create in `:data` module under `br.dev.singular.overview.data.util.mappers.domainToData`.
    - **Domain -> UI:** Create in `:presentation` module under `br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi`.
    - **UI -> Domain:** Create in `:presentation` module under `br.dev.singular.overview.presentation.ui.utils.mappers.uiToDomain`.
- [ ] Step 4: Implement the `toDomain()`, `toData()`, or `toUi()` extension functions.
- [ ] Step 5: For lists or paged data, implement mappers that delegate to the single item mapper (e.g., `List<T>.toUi()`).
- [ ] Step 6: Create a unit test for the mapper to ensure all fields are correctly transformed.
- [ ] Step 7: All new tests must pass successfully.
- [ ] Step 8: Standardize the commit message using the [git-standardizer](../git-standardizer/SKILL.md) skill.

## Architecture Guidelines

Mappers ensure that each layer only knows about the models it's responsible for.

- **Isolation:** Never leak `DataModels` (Retrofit/Room) into the `Domain` or `Presentation` layers.
- **Formatting:** UI-specific formatting (e.g., building full image URLs, date formatting) MUST happen in the `Domain -> UI` mapper.
- **Internal Visibility:** Mappers should be `internal` as they are usually implementation details of the module.
- **Extension Functions:** Prefer extension functions over converter classes for better readability and discoverability.

## Code Examples

### Data -> Domain (in `:data`)
```kotlin
internal fun AppleDataModel.toDomain() = Apple(
    id = id,
    description = rawDescription
)
```

### Domain -> UI (in `:presentation`)
```kotlin
internal fun Apple.toUi() = AppleUiModel(
    id = id,
    displayLabel = "Apple: $description",
    imageURL = buildImageUrl(imagePath)
)
```

## References

For detailed patterns and testing strategies, refer to:

- [Mapper Unit Test Guide](references/UNIT-TEST-GUIDE.md): Testing data transformations.

## Mandatory Rules

- **Location:** Mappers MUST be placed in the predefined `mappers` sub-packages according to the transformation direction.
- **Internal Visibility:** All mappers MUST be `internal`.
- **Naming:** Use standard names: `toDomain()`, `toData()`, or `toUi()`.
- **Plan Approval:** You MUST NOT modify files until the user approves the implementation plan in Step 2.
- **Tests Success:** All new tests MUST pass before completing the task.
