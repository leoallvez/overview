package br.dev.singular.overview.presentation.ui.components.catalog.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.CatalogUiModel
import br.dev.singular.overview.presentation.ui.components.catalog.UiCatalogList
import io.mockk.mockk
import io.mockk.verify
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiCatalogListTest {

    @get:Rule
    val rule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun `UiCatalogList should display title and catalogs`() {
        val catalogName = "Netflix"
        val catalogs = persistentListOf(
            CatalogUiModel(
                id = 1,
                priority = 1,
                logoURL = "",
                name = catalogName,
                previewDrawableRes = null
            )
        )
        rule.setContent {
            UiCatalogList(catalogs = catalogs, isReleased = true)
        }

        rule.onNodeWithText(
            text = context.getString(R.string.where_to_watch),
            substring = true,
            ignoreCase = true
        ).assertIsDisplayed()
        rule.onNodeWithContentDescription(catalogName).assertIsDisplayed()
    }

    @Test
    fun `UiCatalogList should call onClick when a catalog is clicked`() {
        val catalogName = "Netflix"
        val catalogs = persistentListOf(
            CatalogUiModel(
                id = 1L,
                priority = 1,
                logoURL = "",
                name = catalogName,
                previewDrawableRes = null
            )
        )
        val onClick: (CatalogUiModel) -> Unit = mockk(relaxed = true)

        rule.setContent {
            UiCatalogList(
                catalogs = catalogs,
                isReleased = true,
                onClick = onClick
            )
        }

        rule.onNodeWithContentDescription(catalogName).performClick()

        verify { onClick(catalogs.first()) }
    }

    @Test
    fun `UiCatalogList should display empty state when list is empty and released`() {
        rule.setContent {
            UiCatalogList(
                catalogs = emptyList<CatalogUiModel>().toImmutableList(),
                isReleased = true
            )
        }

        rule.onNodeWithText(
            text = context.getString(R.string.empty_list_providers),
            substring = true,
            ignoreCase = true
        ).assertIsDisplayed()
    }

    @Test
    fun `UiCatalogList should display empty state when list is empty and not released`() {
        rule.setContent {
            UiCatalogList(
                catalogs = emptyList<CatalogUiModel>().toImmutableList(),
                isReleased = false
            )
        }

        rule.onNodeWithText(
            text = context.getString(R.string.not_yet_released),
            substring = true,
            ignoreCase = true
        ).assertIsDisplayed()
    }
}
