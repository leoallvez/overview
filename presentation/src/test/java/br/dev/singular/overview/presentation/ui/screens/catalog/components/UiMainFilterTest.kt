package br.dev.singular.overview.presentation.ui.screens.catalog.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.QueryUiState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.RuntimeEnvironment

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiMainFilterTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `UiMainFilter should display filter chips`() {
        val context = RuntimeEnvironment.getApplication()
        val allLabel = context.getString(R.string.all)
        val catalogsLabel = context.getString(R.string.catalogs)
        
        rule.setContent {
            UiMainFilter(query = QueryUiState())
        }
        
        rule.onNodeWithText(allLabel).assertIsDisplayed()
        rule.onNodeWithText(catalogsLabel).assertIsDisplayed()
    }
}
