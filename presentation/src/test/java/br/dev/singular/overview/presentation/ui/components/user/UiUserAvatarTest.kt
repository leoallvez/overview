package br.dev.singular.overview.presentation.ui.components.user

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
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiUserAvatarTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `UiUserAvatar should call onClick when clicked`() {
        val onClick: () -> Unit = mockk(relaxed = true)
        val context = RuntimeEnvironment.getApplication()
        val description = context.getString(R.string.profile)

        rule.setContent {
            UiUserAvatar(
                url = "https://example.com/photo.jpg",
                previewDrawableRes = R.drawable.sample_profile,
                onClick = onClick
            )
        }

        rule.onNodeWithContentDescription(description).performClick()

        verify { onClick() }
    }
}
