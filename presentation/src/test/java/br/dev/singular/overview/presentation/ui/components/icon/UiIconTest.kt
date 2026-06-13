package br.dev.singular.overview.presentation.ui.components.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiIconTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `UiIcon with vector source should render with content description`() {
        val description = "Close Icon"
        rule.setContent {
            UiIcon(
                source = UiIconSource.vector(Icons.Default.Close),
                contentDescription = description
            )
        }
        rule.onNodeWithContentDescription(description).assertIsDisplayed()
    }

    @Test
    fun `UiIcon with painter source should render with content description`() {
        val description = "Alert Icon"
        rule.setContent {
            UiIcon(
                source = UiIconSource.painter(R.drawable.ic_outline_alert),
                contentDescription = description
            )
        }
        rule.onNodeWithContentDescription(description).assertIsDisplayed()
    }
}
