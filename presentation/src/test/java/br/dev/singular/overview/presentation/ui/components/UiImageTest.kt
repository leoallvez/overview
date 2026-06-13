package br.dev.singular.overview.presentation.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiImageTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `UiImage should render with content description`() {
        val description = "Image Desc"
        rule.setContent {
            UiImage(url = "https://example.com/image.jpg", contentDescription = description)
        }
        rule.onNodeWithContentDescription(description).assertIsDisplayed()
    }
}
