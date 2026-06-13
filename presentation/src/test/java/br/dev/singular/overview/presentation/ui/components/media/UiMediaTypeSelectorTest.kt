package br.dev.singular.overview.presentation.ui.components.media

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.singular.overview.presentation.model.MediaUiType
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import br.dev.singular.overview.presentation.R

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiMediaTypeSelectorTest {

    @get:Rule
    val rule = createComposeRule()

    private val context: Context by lazy {
        ApplicationProvider.getApplicationContext()
    }

    @Test
    fun `UiMediaTypeSelector should display all types`() {
        rule.setContent {
            UiMediaTypeSelector(type = MediaUiType.ALL)
        }

        MediaUiType.entries.forEach { type ->
            val label = context.getString(type.labelRes)
            rule.onNodeWithText(label).assertIsDisplayed()
        }
    }

    @Test
    fun `UiMediaTypeSelector should call onClick with correct type when clicked`() {
        val onClick: (MediaUiType) -> Unit = mockk(relaxed = true)
        val allLabel = context.getString(R.string.all)
        val movieLabel = context.getString(R.string.movies)
        val tvLabel = context.getString(R.string.tv_show)

        rule.setContent {
            UiMediaTypeSelector(type = MediaUiType.MOVIE, onClick = onClick)
        }

        // Test clicking All (to go back to ALL)
        rule.onNodeWithText(allLabel).performClick()
        verify { onClick(MediaUiType.ALL) }

        // Test clicking Movie
        rule.onNodeWithText(movieLabel).performClick()
        verify { onClick(MediaUiType.MOVIE) }

        // Test clicking TV
        rule.onNodeWithText(tvLabel).performClick()
        verify { onClick(MediaUiType.TV) }
    }
}
