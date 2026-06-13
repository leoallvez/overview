package br.dev.singular.overview.presentation.ui.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiPersonAvatarTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `UiPersonAvatar should be rendered`() {
        val tag = "person_avatar"
        rule.setContent {
            UiPersonAvatar(
                url = "",
                size = 40.dp,
                modifier = Modifier.testTag(tag)
            )
        }
        rule.onNodeWithTag(tag).assertIsDisplayed()
    }
}
