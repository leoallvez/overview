package br.dev.singular.overview.presentation.ui.components.video.item

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.singular.overview.presentation.ui.components.shimmer.UiShimmerProvider
import br.dev.singular.overview.presentation.ui.components.video.UiVideoItem
import br.dev.singular.overview.presentation.ui.components.video.UiVideoItemSkeleton
import br.dev.singular.overview.presentation.ui.utils.fakeVideo
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiVideoItemTest {

    @get:Rule
    val rule = createComposeRule()

    private val fakeVideo = fakeVideo()

    @Test
    fun `UiVideoItem should display name and respond to click`() {
        val tag = "video_item"
        val onClick: (String) -> Unit = mockk(relaxed = true)
        rule.setContent {
            UiVideoItem(
                modifier = Modifier.testTag(tag),
                video = fakeVideo,
                onClick = onClick
            )
        }

        rule.onNodeWithText(fakeVideo.name).assertIsDisplayed()
        rule.onNodeWithTag(tag).performClick()
        verify { onClick(fakeVideo.key) }
    }

    @Test
    fun `UiVideoItemSkeleton should be rendered`() {
        val tag = "video_item_skeleton"
        rule.setContent {
            UiShimmerProvider {
                UiVideoItemSkeleton(modifier = Modifier.testTag(tag))
            }
        }
        rule.onNodeWithTag(tag).assertIsDisplayed()
    }
}
