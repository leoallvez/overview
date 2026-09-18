package br.dev.singular.overview.presentation.ui.components.button

import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.singular.overview.presentation.R
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiGoogleButtonTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `UiGoogleButton should call onClick when clicked`() {
        val onClick: () -> Unit = mockk(relaxed = true)
        val context = RuntimeEnvironment.getApplication()
        val text = context.getString(R.string.sign_in_with_google)

        rule.setContent {
            UiGoogleButton(onClick = onClick)
        }

        rule.onNodeWithText(text).performClick()

        verify { onClick() }
    }
}
