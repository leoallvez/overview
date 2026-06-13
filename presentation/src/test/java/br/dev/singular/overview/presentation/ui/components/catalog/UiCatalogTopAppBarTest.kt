package br.dev.singular.overview.presentation.ui.components.catalog

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.assertCountEquals
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.singular.overview.presentation.model.CatalogUiModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiCatalogTopAppBarTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `when catalog is not null, should display catalog name`() {
        val catalog = CatalogUiModel(
            id = 1,
            priority = 1,
            logoURL = "",
            name = "Catalog Header",
            previewDrawableRes = null
        )
        rule.setContent {
            UiCatalogTopAppBar(catalog = catalog, isCollapsed = false)
        }
        rule.onNodeWithText(catalog.name).assertIsDisplayed()
    }

    @Test
    fun `when catalog is null, should render only loading state`() {
        val tag = "app_bar"

        rule.setContent {
            UiCatalogTopAppBar(
                catalog = null, 
                isCollapsed = false,
                modifier = Modifier.testTag(tag)
            )
        }

        rule.onNodeWithTag(tag, useUnmergedTree = true)
            .onChildren()
            .assertCountEquals(2)
    }
}
