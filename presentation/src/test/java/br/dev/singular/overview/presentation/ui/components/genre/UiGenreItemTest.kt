package br.dev.singular.overview.presentation.ui.components.genre

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.model.GenreUiModel
import br.dev.singular.overview.presentation.ui.utils.getColor
import br.dev.singular.overview.presentation.ui.utils.getImageVector
import br.dev.singular.overview.presentation.ui.utils.localizedName
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Zap
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiGenreItemTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `UiGenreItem should display name and respond to click`() {
        val onClick: () -> Unit = mockk(relaxed = true)
        val genre = GenreUiModel(id = 1, name = "Action")

        rule.setContent {
            UiGenreItem(model = genre, selected = false, onClick = onClick)
        }

        rule.onNodeWithText(genre.name).assertIsDisplayed()
        rule.onNodeWithText(genre.name).performClick()
        verify { onClick() }
    }

    @Test
    fun `getImageVector should return Zap icon for Action genre ID`() {
        val genre = GenreUiModel(id = 28L, name = "Action")
        var icon: ImageVector? = null

        rule.setContent {
            icon = genre.getImageVector()
        }

        assertEquals(Lucide.Zap, icon)
    }

    @Test
    fun `localizedName should return string resource for known genre ID`() {
        val genre = GenreUiModel(id = 28L, name = "Some Name")
        val expectedString = RuntimeEnvironment.getApplication().getString(R.string.genre_action)
        var resultName = ""

        rule.setContent {
            resultName = genre.localizedName()
        }

        assertEquals(expectedString, resultName)
    }

    @Test
    fun `localizedName should return name field when labelRes is 0`() {
        val genre = GenreUiModel(id = 0, name = "Custom Genre")
        var resultName = ""

        rule.setContent {
            resultName = genre.localizedName()
        }

        assertEquals("Custom Genre", resultName)
    }

    @Test
    fun `getColor should return expected color for Action genre ID`() {
        val genre = GenreUiModel(id = 28L, name = "Action")
        var color: Color = Color.Unspecified

        rule.setContent {
            color = genre.getColor()
        }

        val expected = Color(0xFFFF7043)
        assertEquals(expected, color)
    }

    @Test
    fun `getColor should return White for unknown genre ID`() {
        val genre = GenreUiModel(id = 9999L, name = "Unknown")
        var color: Color = Color.Unspecified

        rule.setContent {
            color = genre.getColor()
        }

        assertEquals(Color.White, color)
    }
}
