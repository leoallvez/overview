package br.dev.singular.overview.presentation.ui.components.catalog

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.isSelectable
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.singular.overview.presentation.model.CatalogUiModel
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiCatalogItemTest {

    @get:Rule
    val rule = createComposeRule()

    private val fakeCatalog = CatalogUiModel(
        id = 1,
        priority = 1,
        logoURL = "",
        name = "Test Item",
        previewDrawableRes = null
    )

    @Test
    fun `when selected is null, should display name and respond to click`() {
        val onClick: () -> Unit = mockk(relaxed = true)
        rule.setContent {
            UiCatalogItem(model = fakeCatalog, selected = null, onClick = onClick)
        }
        rule.onNodeWithText(fakeCatalog.name).assertIsDisplayed()
        rule.onNodeWithText(fakeCatalog.name).performClick()
        verify { onClick() }
    }

    @Test
    fun `when selected is true, RadioButton should be selected`() {
        rule.setContent {
            UiCatalogItem(model = fakeCatalog, selected = true)
        }
        rule.onNode(isSelectable()).assertIsSelected()
    }

    @Test
    fun `when selected is false, RadioButton should not be selected`() {
        rule.setContent {
            UiCatalogItem(model = fakeCatalog, selected = false)
        }
        rule.onNode(isSelectable()).assertIsNotSelected()
    }
}
