package br.dev.singular.overview.presentation.ui.components

import android.content.Context
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.singular.overview.presentation.R
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@OptIn(ExperimentalTestApi::class)
@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiSearchFieldTest {

    @get:Rule
    val rule = createComposeRule()

    private val context: Context by lazy {
        ApplicationProvider.getApplicationContext()
    }

    @Test
    fun `when query is empty, placeholder should be visible`() {
        with(rule) {
            // arrange
            val placeholder = "Search for movies"
            // act
            setContent {
                UiSearchField(
                    query = "",
                    placeholder = placeholder,
                )
            }
            // assert
            onNodeWithText(placeholder).assertIsDisplayed()
        }
    }

    @Test
    fun `when query is not empty, clear icon should be visible and clickable`() {
        with(rule) {
            // arrange
            val closeDescription = context.getString(R.string.close)
            val onQueryChange: (String) -> Unit = mockk(relaxed = true)
            val onClear: () -> Unit = mockk(relaxed = true)
            val query = "Batman"
            // act
            setContent {
                UiSearchField(
                    query = query,
                    onQueryChange = onQueryChange,
                    onClear = onClear
                )
            }
            onNodeWithContentDescription(closeDescription).performClick()
            // assert
            verify { onQueryChange("") }
            verify { onClear() }
        }
    }

    @Test
    fun `when typing text, onQueryChange should be called`() {
        with(rule) {
            // arrange
            val placeholder = context.getString(R.string.search)
            val onQueryChange: (String) -> Unit = mockk(relaxed = true)
            val inputText = "Spider-man"
           // act
            setContent {
                UiSearchField(
                    query = "",
                    onQueryChange = onQueryChange
                )
            }
            onNodeWithText(placeholder).performTextInput(inputText)
            // assert
            verify { onQueryChange(inputText) }
        }
    }

    @Test
    fun `when search action is performed, onSearchAction should be called`() {
        with(rule) {
            // arrange
            val onSearch: () -> Unit = mockk(relaxed = true)
            // act
            setContent {
                UiSearchField(
                    query = "Superman",
                    onSearch = onSearch
                )
            }
            onNodeWithText("Superman").performImeAction()
            // assert
            verify { onSearch() }
        }
    }
}
