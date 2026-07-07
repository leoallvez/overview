package br.dev.singular.overview.presentation.ui.components.person.item

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.singular.overview.presentation.ui.components.person.UiPersonItem
import br.dev.singular.overview.presentation.ui.components.person.UiPersonItemSkeleton
import br.dev.singular.overview.presentation.ui.components.shimmer.UiShimmerProvider
import br.dev.singular.overview.presentation.ui.utils.fakePerson
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiPersonItemTest {

    @get:Rule
    val rule = createComposeRule()

    private val fakePerson = fakePerson()

    @Test
    fun `UiPersonItem should display name, description and respond to click`() {
        val onClick: (Long) -> Unit = mockk(relaxed = true)
        rule.setContent {
            UiPersonItem(model = fakePerson, onClick = onClick)
        }

        rule.onNodeWithText(fakePerson.name).assertIsDisplayed()
        rule.onNodeWithText(fakePerson.description).assertIsDisplayed()
        rule.onNodeWithText(fakePerson.name).performClick()
        verify { onClick(fakePerson.id) }
    }

    @Test
    fun `UiPersonItemSkeleton should be rendered`() {
        val tag = "person_item_skeleton"
        rule.setContent {
            UiShimmerProvider {
                UiPersonItemSkeleton(modifier = Modifier.testTag(tag))
            }
        }
        rule.onNodeWithTag(tag).assertIsDisplayed()
    }
}
