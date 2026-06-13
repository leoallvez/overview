package br.dev.singular.overview.presentation.ui.components.media

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.model.MediaUiType
import br.dev.singular.overview.presentation.ui.components.shimmer.UiShimmerProvider
import io.mockk.mockk
import io.mockk.verify
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@OptIn(ExperimentalTestApi::class)
@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiMediaGridTest {

    @get:Rule
    val rule = createComposeRule()

    private val fakeMedia = MediaUiModel(
        id = 1,
        title = "Batman",
        type = MediaUiType.MOVIE,
        posterURL = "",
        previewDrawableRes = null
    )

    @Test
    fun `UiMediaGrid should display items when using simple list`() {
        val items = persistentListOf(fakeMedia)
        rule.setContent {
            UiMediaGrid(items = items)
        }
        rule.onNodeWithText(fakeMedia.title).assertIsDisplayed()
    }

    @Test
    fun `UiMediaGrid should call onClick when an item is clicked`() {
        val items = persistentListOf(fakeMedia)
        val onClick: (MediaUiModel) -> Unit = mockk(relaxed = true)
        
        rule.setContent {
            UiMediaGrid(items = items, onClick = onClick)
        }

        rule.onNodeWithText(fakeMedia.title).performClick()
        
        verify { onClick(fakeMedia) }
    }

    @Test
    fun `UiMediaGridSkeleton should be rendered`() {
        val tag = "media_grid_skeleton"
        rule.setContent {
            UiShimmerProvider {
                UiMediaGridSkeleton(modifier = Modifier.testTag(tag))
            }
        }
        rule.onNodeWithTag(tag).assertIsDisplayed()
    }
}
