package br.dev.singular.overview.presentation.ui.components.snackbar

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiSnackbarHostTest {

    @get:Rule
    val rule = createComposeRule()

    private val hostState = UiSnackbarHostState()

    @Test
    fun `UiSnackbarHost should keep a stacked snackbar visible when an older one expires`() {
        rule.mainClock.autoAdvance = false
        rule.setContent { UiSnackbarHost(hostState = hostState) }

        showSnackbar(message = "First")
        rule.mainClock.advanceTimeBy(2_000)
        showSnackbar(message = "Second")
        rule.mainClock.advanceTimeBy(2_500)

        rule.onNodeWithText("First").assertDoesNotExist()
        rule.onNodeWithText("Second").assertIsDisplayed()

        rule.mainClock.advanceTimeBy(2_500)

        rule.onNodeWithText("Second").assertDoesNotExist()
        assertTrue(hostState.snackbarList.isEmpty())
    }

    @Test
    fun `UiSnackbarHostState should dismiss only the given entry when visuals are equal`() {
        val visuals = createVisuals(message = "Same")
        hostState.show(visuals)
        hostState.show(visuals)
        val (first, second) = hostState.snackbarList

        hostState.dismiss(second)

        assertEquals(listOf(first.id), hostState.snackbarList.map { it.id })
    }

    @Test
    fun `UiSnackbarHost should keep an indefinite snackbar until it is dismissed`() {
        rule.mainClock.autoAdvance = false
        rule.setContent { UiSnackbarHost(hostState = hostState) }
        val visuals = UiSnackbarVisuals.Persistent(title = "Title", message = "Indefinite")

        rule.runOnUiThread { hostState.show(visuals) }
        rule.waitForIdle()
        rule.mainClock.advanceTimeBy(60_000)

        rule.onNodeWithText("Indefinite").assertIsDisplayed()

        rule.runOnUiThread { hostState.dismiss(visuals) }
        rule.waitForIdle()
        rule.mainClock.advanceTimeBy(1_000)

        rule.onNodeWithText("Indefinite").assertDoesNotExist()
        assertTrue(hostState.snackbarList.isEmpty())
    }

    @Test
    fun `UiSnackbarHostState should not request the dismissal of other visuals`() {
        hostState.show(createVisuals(message = "First"))
        hostState.show(createVisuals(message = "Second"))
        val (first, second) = hostState.snackbarList

        hostState.dismiss(second.visuals)

        assertEquals(listOf(false, true), listOf(first.dismissRequested, second.dismissRequested))
    }

    private fun showSnackbar(message: String) {
        rule.runOnUiThread { hostState.show(createVisuals(message)) }
        rule.waitForIdle()
    }

    private fun createVisuals(message: String) = UiSnackbarVisuals.Close(
        title = "Title",
        message = message
    )
}
