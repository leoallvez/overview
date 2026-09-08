# Compose Component Unit Test Guide

This reference provides examples for creating unit tests for UI components, including skeleton variants, in the **Overview** project.

## UI Component Unit Test

Component unit tests verify the rendering and interaction logic of Composables using `ComposeTestRule` and Robolectric.

### Example: `UiAppleCardTest.kt`

```kotlin
@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiAppleCardTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `should display description when model is provided`() {
        val model = AppleUiModel(id = 1, description = "Delicious Apple")

        rule.setContent {
            UiAppleCard(model = model)
        }

        rule.onNodeWithText("Delicious Apple").assertIsDisplayed()
    }

    @Test
    fun `should trigger onClick when card is clicked`() {
        var clicked = false
        val model = AppleUiModel(id = 1, description = "Delicious Apple")

        rule.setContent {
            UiAppleCard(
                model = model,
                onClick = { clicked = true }
            )
        }

        rule.onNodeWithText("Delicious Apple").performClick()

        assertTrue(clicked)
    }

    @Test
    fun `skeleton should render without content text`() {
        val tag = "apple_card_skeleton"
        rule.setContent {
            UiAppleCardSkeleton(
                modifier = Modifier.testTag(tag)
            )
        }

        // Verify skeleton structure exists
        rule.onNodeWithTag(tag).assertIsDisplayed()
        
        // Verify real content is NOT present
        rule.onNodeWithText("Delicious Apple").assertDoesNotExist()
    }
}
```

### Key Requirements:
- **Rule:** Use `createComposeRule()`.
- **Finding Nodes:** Use `onNodeWithText()` for content validation and `onNodeWithTag()` for structural validation.
- **Skeleton Testing:** Verify the skeleton appears (via `testTag`) and that specific content from the real component is absent.
- **Assertions:** Use `assertIsDisplayed()`, `assertDoesNotExist()`, or `assertTextEquals()`.
- **Interactions:** Use `performClick()` to simulate user actions.
