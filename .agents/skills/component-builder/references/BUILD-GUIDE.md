# UI Component Build Guide

This reference demonstrates the standard pattern for creating a UI component and its loading state (skeleton) in the **Overview** project.

### 1. UI Component Structure

A standard component should be stateless (where possible), use the `Ui` prefix, and include a custom preview.

```kotlin
/**
 * Component to display an Apple Card with its description.
 *
 * @param model The UI model containing apple data.
 * @param modifier The modifier to be applied to the layout.
 * @param onClick Callback invoked when the card is clicked.
 */
@Composable
fun UiAppleCard(
    model: AppleUiModel,
    modifier: Modifier = Modifier,
    onClick: (AppleUiModel) -> Unit = {}
) {
    Card(
        modifier = modifier
            .clickable { onClick.invoke(model) },
        colors = CardDefaults.cardColors(
            containerColor = Background
        ),
    ) {
        Box(Modifier.padding(dimensionResource(R.dimen.spacing_2x))) {
            UiText(text = model.description)
        }
    }
}
```

### 2. Skeleton Component

Complex components should provide a skeleton variant to be used during loading states. Use `UiShimmerBox` to mimic the component's layout.

```kotlin
/**
 * A skeleton/loading state for [UiAppleCard].
 *
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
fun UiAppleCardSkeleton(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Background
        ),
    ) {
        Box(Modifier.padding(dimensionResource(R.dimen.spacing_2x))) {
            UiShimmerBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.spacing_3x))
            )
        }
    }
}
```

### 3. Previews

Use the `@UiComponentPreview` annotation for internal previews. Include both states.

```kotlin
@UiComponentPreview
@Composable
internal fun UiAppleCardPreview() {
    val model = AppleUiModel(id = 1, description = "Description")
    UiAppleCard(model)
}

@UiComponentPreview
@Composable
internal fun UiAppleCardSkeletonPreview() {
    UiAppleCardSkeleton()
}
```

### Key Requirements:
1.  **Prefix:** Use the `Ui` prefix for both the Composable and its Skeleton (e.g., `UiAppleCard`, `UiAppleCardSkeleton`).
2.  **Design System:** Use project-specific components like `UiText`, `UiShimmerBox`, and dimension resources.
3.  **Skeleton Layout:** The skeleton should have the same dimensions and basic structure as the original component.
4.  **Statelessness:** Hoist state to callers or ViewModels.
