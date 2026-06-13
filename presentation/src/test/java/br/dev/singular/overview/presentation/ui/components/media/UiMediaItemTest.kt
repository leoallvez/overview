package br.dev.singular.overview.presentation.ui.components.media

import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.model.MediaUiType
import br.dev.singular.overview.presentation.ui.components.shimmer.UiShimmerProvider
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiMediaItemTest {

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
    fun `UiMediaItem should display title and respond to click`() {
        val onClick: (MediaUiModel) -> Unit = mockk(relaxed = true)
        rule.setContent {
            UiMediaItem(model = fakeMedia, onClick = onClick)
        }
        rule.onNodeWithText(fakeMedia.title).assertIsDisplayed()
        rule.onNodeWithText(fakeMedia.title).performClick()
        verify { onClick(fakeMedia) }
    }

    @Test
    fun `UiMediaItemSkeleton should be rendered`() {
        val tag = "media_item_skeleton"
        rule.setContent {
            UiShimmerProvider {
                Box(Modifier.testTag(tag)) {
                    UiMediaItemSkeleton()
                }
            }
        }
        rule.onNodeWithTag(tag).assertIsDisplayed()
    }
}
