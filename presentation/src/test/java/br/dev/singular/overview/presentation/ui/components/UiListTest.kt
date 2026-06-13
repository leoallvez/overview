package br.dev.singular.overview.presentation.ui.components

import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiListTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `UiList should render firstItem and all items in the list`() {
        val firstItemText = "Header Item"
        val items = persistentListOf("Item 1", "Item 2", "Item 3")
        val listTag = "ui_list"

        rule.setContent {
            UiList(
                items = items,
                modifier = Modifier.testTag(listTag),
                firstItem = { Text(text = firstItemText) }
            ) { item ->
                Text(text = item)
            }
        }

        // Verify firstItem is displayed
        rule.onNodeWithText(firstItemText).assertIsDisplayed()

        // Verify all list items are displayed
        items.forEach { item ->
            rule.onNodeWithText(item).assertIsDisplayed()
        }

        // Verify total children count in the LazyColumn (unmerged)
        // 1 (firstItem) + 3 (items) = 4
        rule.onNodeWithTag(listTag, useUnmergedTree = true)
            .onChildren()
            .assertCountEquals(4)
    }

    @Test
    fun `UiList should render only firstItem when list is empty`() {
        val firstItemText = "Empty List Header"
        val items = persistentListOf<String>()
        val listTag = "ui_list"

        rule.setContent {
            UiList(
                items = items,
                modifier = Modifier.testTag(listTag),
                firstItem = { Text(text = firstItemText) }
            ) { item ->
                Text(text = item)
            }
        }

        rule.onNodeWithText(firstItemText).assertIsDisplayed()
        
        rule.onNodeWithTag(listTag, useUnmergedTree = true)
            .onChildren()
            .assertCountEquals(1)
    }

    @Test
    fun `UiList should correctly pass generic data to content lambda`() {
        data class TestData(val id: Int, val label: String)
        val items = persistentListOf(TestData(1, "Alpha"), TestData(2, "Beta"))

        rule.setContent {
            UiList(items = items) { data ->
                Text(text = "ID: ${data.id} - ${data.label}")
            }
        }

        rule.onNodeWithText("ID: 1 - Alpha").assertIsDisplayed()
        rule.onNodeWithText("ID: 2 - Beta").assertIsDisplayed()
    }
}
