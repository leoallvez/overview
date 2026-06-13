package br.dev.singular.overview.presentation.ui.components.catalog

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.singular.overview.presentation.model.CatalogUiModel
import br.dev.singular.overview.presentation.ui.components.text.UiText
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiCatalogCardTest {

    @get:Rule
    val rule = createComposeRule()

    private val fakeCatalog = CatalogUiModel(
        id = 1,
        priority = 1,
        logoURL = "",
        name = "Test Catalog",
        previewDrawableRes = null
    )

    @Test
    fun `UiCatalogCard should display name, respond to click and render right content`() {
        val onClick: () -> Unit = mockk(relaxed = true)
        val rightContentText = "Right Content"
        val cardTag = "catalog_card"
        
        rule.setContent {
            UiCatalogCard(
                model = fakeCatalog,
                onClick = onClick,
                modifier = Modifier.testTag(cardTag)
            ) {
                UiText(text = rightContentText, modifier = Modifier.testTag("right_content"))
            }
        }
        
        rule.onNodeWithTag(cardTag).assertIsDisplayed()
        rule.onNodeWithText(fakeCatalog.name, useUnmergedTree = true).assertIsDisplayed()
        rule.onNodeWithTag("right_content", useUnmergedTree = true).assertIsDisplayed()
        
        rule.onNodeWithText(fakeCatalog.name, useUnmergedTree = true).performClick()
        verify { onClick() }
    }
}
