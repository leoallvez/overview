package br.dev.singular.overview.presentation.ui.components.video.list

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.singular.overview.presentation.ui.components.shimmer.UiShimmerProvider
import br.dev.singular.overview.presentation.ui.components.video.UiVideoList
import br.dev.singular.overview.presentation.ui.components.video.UiVideoListSkeleton
import br.dev.singular.overview.presentation.ui.utils.fakeVideo
import io.mockk.mockk
import io.mockk.verify
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiVideoListTest {

    @get:Rule
    val rule = createComposeRule()

    private val fakeVideo = fakeVideo()

    @Test
    fun `UiVideoList should display videos and respond to click`() {
        val tag = "video_list"
        val videos = persistentListOf(fakeVideo)
        val onClick: (String) -> Unit = mockk(relaxed = true)

        rule.setContent {
            UiVideoList(
                modifier = Modifier.testTag(tag),
                videos = videos,
                onClick = onClick
            )
        }

        rule.onNodeWithText(fakeVideo.name).assertIsDisplayed()
        rule.onNodeWithTag(tag).performClick()

        verify { onClick(fakeVideo.key) }
    }

    @Test
    fun `UiVideoList should not be displayed when list is empty`() {
        val tag = "video_list"
        rule.setContent {
            UiVideoList(
                modifier = Modifier.testTag(tag),
                videos = persistentListOf()
            )
        }

        rule.onNodeWithTag(tag).assertDoesNotExist()
    }

    @Test
    fun `UiVideoListSkeleton should be rendered`() {
        val tag = "video_list_skeleton"
        rule.setContent {
            UiShimmerProvider {
                UiVideoListSkeleton(modifier = Modifier.testTag(tag))
            }
        }
        rule.onNodeWithTag(tag).assertIsDisplayed()
    }
}
