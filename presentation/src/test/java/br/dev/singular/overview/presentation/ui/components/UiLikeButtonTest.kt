package br.dev.singular.overview.presentation.ui.components

import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.singular.overview.presentation.R
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.RuntimeEnvironment

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiLikeButtonTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `UiLikeButton should call onClick when clicked`() {
        val onClick: () -> Unit = mockk(relaxed = true)
        val description = RuntimeEnvironment.getApplication().getString(R.string.like_button)
        rule.setContent {
            UiLikeButton(isLiked = false, onClick = onClick)
        }
        rule.onNodeWithContentDescription(description).performClick()
        verify { onClick() }
    }
}
