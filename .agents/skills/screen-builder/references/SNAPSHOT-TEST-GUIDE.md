# Screen Snapshot Test Guide

This reference provides examples for creating snapshot tests for full screens in the **Overview** project using **Paparazzi**.

## Snapshot Testing Overview

Snapshot tests for screens ensure that the entire layout, including the Scaffold, TopAppBar, and content area, renders correctly across different device configurations.

### Example: `AppleDetailSnapshotTest.kt`

```kotlin
class AppleDetailSnapshotTest : UiSnapshotTest(snapshotPackage = "screens/apple") {

    @Test
    fun success() = snapshot {
        AppleDetailScreenSuccessPreview()
    }

    @Test
    fun loading() = snapshot {
        AppleDetailScreenLoadingPreview()
    }

    @Test
    fun error() = snapshot {
        AppleDetailScreenErrorPreview()
    }
}
```

### Key Requirements:
- **Base Class:** Extend `UiSnapshotTest`. Pass a unique `snapshotPackage` name (e.g., `screens/feature_name`) to the constructor.
- **Preview Usage:** Use `@UiScreenPreview` annotated functions to test the "Success" state.
- **State Coverage:** Explicitly test `Loading` and `Error` states by manually providing the `uiState` to the screen composable.
- **Naming:** Use the `name` parameter in `snapshot(name = "...")` to distinguish between `success`, `loading`, and `error` states.
- **Device Configurations:** Since screens are complex, the `UiSnapshotTest` might be configured to run against multiple device profiles (Phone, Tablet). Ensure your screen handles these variations gracefully.
- **Verification:**
    - To **record** new snapshots: Run `./gradlew :presentation:recordPaparazziDebug`.
    - To **verify** changes: Run `./gradlew :presentation:verifyPaparazziDebug`.
