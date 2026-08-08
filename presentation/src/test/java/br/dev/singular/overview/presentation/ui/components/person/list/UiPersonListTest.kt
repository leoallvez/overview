package br.dev.singular.overview.presentation.ui.components.person.list

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.singular.overview.presentation.ui.components.person.UiPersonList
import br.dev.singular.overview.presentation.ui.components.person.UiPersonListSkeleton
import br.dev.singular.overview.presentation.ui.components.shimmer.UiShimmerProvider
import br.dev.singular.overview.presentation.ui.utils.fakePerson
import io.mockk.mockk
import io.mockk.verify
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiPersonListTest {

    @get:Rule
    val rule = createComposeRule()

    private val fakePerson = fakePerson()
    
    @Test
    fun `UiPersonList should display title and items`() {
        val listTitle = "Cast"
        val people = persistentListOf(fakePerson)
        rule.setContent {
            UiPersonList(title = listTitle, people = people)
        }
        rule.onNodeWithText(listTitle).assertIsDisplayed()
        rule.onNodeWithText(fakePerson.name).assertIsDisplayed()
    }

    @Test
    fun `UiPersonList should call onClickItem when a person item is clicked`() {
        val listTitle = "Cast"
        val people = persistentListOf(fakePerson)
        val onClickItem: (Long) -> Unit = mockk(relaxed = true)

        rule.setContent {
            UiPersonList(title = listTitle, people = people, onClick = onClickItem)
        }

        rule.onNodeWithText(fakePerson.name).performClick()

        verify { onClickItem(fakePerson.id) }
    }

    @Test
    fun `UiPersonListSkeleton should be rendered`() {
        val tag = "person_list_skeleton"
        rule.setContent {
            UiShimmerProvider {
                UiPersonListSkeleton(modifier = Modifier.testTag(tag))
            }
        }
        rule.onNodeWithTag(tag).assertIsDisplayed()
    }
}
