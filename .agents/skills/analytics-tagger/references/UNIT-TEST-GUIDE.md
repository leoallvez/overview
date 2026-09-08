# Analytics Unit Test Guide

This reference provides the standard pattern for creating unit tests for Analytics in the **Overview** project using **MockK**.

## Testing Overview

Analytics unit tests verify that the `TagManager` is called with the correct event names, paths, and parameters when specific actions occur.

### Example: `AppleActionsTest.kt`

```kotlin
class AppleActionsTest {

    private val onBack: () -> Unit = mockk(relaxed = true)
    private val tagPath = "/apple-details"
    private val sut = AppleActions(tagPath, onBack)

    @Before
    fun setup() {
        mockkObject(TagManager)
    }

    @After
    fun tearDown() {
        unmockkObject(TagManager)
    }

    @Test
    fun `handleBack should log click and trigger onBack`() {
        // When
        sut.handleBack()

        // Then
        verify(exactly = 1) { 
            TagManager.logClick(tagPath, TagCommon.Detail.BACK) 
        }
        verify(exactly = 1) { onBack() }
    }
}
```

### Key Requirements:

- **Mocking Singleton:** Use `mockkObject(TagManager)` in `@Before` and `unmockkObject(TagManager)` in `@After` to intercept calls to the object.
- **Verification:** Use `verify` to check that `logClick`, `logScreenView`, or `logInteraction` was called with the exact expected strings.
- **Integration with Actions:** Most interaction analytics should be tested by triggering a function in the `Actions` class and verifying the corresponding `TagManager` call.
- **Status Mapping:** For screen views, test that different `UiState` values result in the correct `TagStatus` being logged.
