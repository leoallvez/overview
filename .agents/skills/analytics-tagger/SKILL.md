---
name: analytics-tagger
description: It implements analytics tracking (Firebase) for screens and interactions. Use it to track user behavior, screen views, and clicks following the project's tagging standards.
metadata:
  author: Singular
  version: "1.0"
  keywords:
  - analytics
  - firebase
  - tagging
  - track-screen
  - log-event
---

## Core Workflow

- [ ] Step 1: Identify the screen or interaction to be tracked. Ask the user for the event type (Screen View, Click, or Interaction) and specific requirements.
- [ ] Step 2: Create a plan detailing the `tagPath` and parameters (like `status` or `detail`) to be used. Present it for approval.
- [ ] Step 3: Define or update the Tag Path:
    - For screens, usually defined in the `Interaction` layer of the feature (e.g., `AppleActions` or a dedicated `AppleTagPath` class).
    - Use descriptive, lowercase paths (e.g., `/apple-details`).
- [ ] Step 4: Track Screen Views in Composables:
    - Use the `TrackScreenView(tagPath, status)` Composable.
    - Pass a standard status from `TagStatus` (e.g., `SUCCESS`, `LOADING`, `ERROR`).
- [ ] Step 5: Track Clicks and Interactions:
    - Call `TagManager.logClick(tagPath, detail, id)` or `TagManager.logInteraction(tagPath, detail)`.
    - Use standard details from `TagCommon.Detail` (like `BACK`, `CLOSE`) when applicable.
    - Trigger these calls within the `Actions` class or ViewModel intents.
- [ ] Step 6: Create a unit test to verify that the `TagManager` is called with the correct parameters.
- [ ] Step 7: All new tests must pass successfully.
- [ ] Step 8: Standardize the commit message using the [git-standardizer](../git-standardizer/SKILL.md) skill.

## Architecture Guidelines

Tagging is managed via the `TagManager` singleton and standard parameter objects.

- **Centralization:** Use `TagManager` for all tracking. Do not call Firebase APIs directly in features.
- **Stateless Tracking:** Use `TrackScreenView` inside Composables to handle the `DisposableEffect` lifecycle automatically.
- **Parameter Standardization:** Use `TagStatus` for screen statuses and `TagCommon` for shared interaction details.
- **Path Consistency:** Paths should start with `/` and use kebab-case.

## Code Examples

### Tracking Screen View
```kotlin
@Composable
fun AppleScreen(uiState: UiState<Apple>, actions: AppleActions) {
    val status = when (uiState) {
        is UiState.Success -> TagStatus.SUCCESS
        is UiState.Error -> TagStatus.ERROR
        else -> TagStatus.LOADING
    }
    
    TrackScreenView(tagPath = actions.tagPath, status = status)
    
    // ... rest of the screen
}
```

### Tracking Click
```kotlin
data class AppleActions(
    val tagPath: String,
    val onBack: () -> Unit
) {
    fun handleBack() {
        TagManager.logClick(tagPath, TagCommon.Detail.BACK)
        onBack()
    }
}
```

## References

For detailed patterns and testing strategies, refer to:

- [Analytics Unit Test Guide](references/UNIT-TEST-GUIDE.md): Verifying event logging.

## Mandatory Rules

- **Wrapper Usage:** MUST use `TagManager` or `TrackScreenView`. NEVER use `FirebaseAnalytics` directly in UI code.
- **Standard Constants:** MUST use `TagStatus` and `TagCommon` constants instead of hardcoded strings for common values.
- **Plan Approval:** You MUST NOT modify files until the user approves the tagging plan in Step 2.
- **Tests Success:** All new tests MUST pass before completing the task.
