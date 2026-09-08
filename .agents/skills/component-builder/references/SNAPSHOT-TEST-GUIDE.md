# Compose Component Snapshot Test Guide

This reference provides examples for creating snapshot tests for UI components, including skeletons, in the **Overview** project using **Paparazzi**.

## Snapshot Testing Overview

Snapshot tests capture the visual rendering of a component and compare it against a "golden" image to detect unintended UI regressions.

### Example: `UiAppleCardSnapshotTest.kt`

```kotlin
class UiAppleCardSnapshotTest : UiSnapshotTest(snapshotPackage = "components/apple_card") {

    @Test
    fun default() = snapshot {
        UiAppleCardPreview()
    }

    @Test
    fun skeleton() = snapshot {
        UiAppleCardSkeletonPreview()
    }
}
```

### Key Requirements:
- **Base Class:** Extend `UiSnapshotTest`. Pass a unique `snapshotPackage` name (usually the component folder path) to the constructor.
- **Preview Usage:** Prefer calling the internal `@UiComponentPreview` methods directly in the `snapshot` block.
- **Naming:** Use the `name` parameter in `snapshot(name = "...")` to distinguish between different states (e.g., "skeleton", "error", "long_text").
- **Theme:** The `snapshot` method automatically wraps the content in `AppTheme`.
- **Verification:**
    - To **record** new snapshots: Run `./gradlew :presentation:recordPaparazziDebug`.
    - To **verify** changes: Run `./gradlew :presentation:verifyPaparazziDebug`.
- **Golden Images:** Images are stored in `presentation/src/test/snapshots/`.
