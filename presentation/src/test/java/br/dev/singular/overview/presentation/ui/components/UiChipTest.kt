package br.dev.singular.overview.presentation.ui.components

import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiChipTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `UiChip should call onClick when clicked`() {
        val onClick: () -> Unit = mockk(relaxed = true)
        val text = "Chip Text"
        rule.setContent {
            UiChip(text = text, onClick = onClick)
        }
        rule.onNodeWithText(text).performClick()
        verify { onClick() }
    }
}
