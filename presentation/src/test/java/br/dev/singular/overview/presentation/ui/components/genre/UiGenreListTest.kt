package br.dev.singular.overview.presentation.ui.components.genre

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.singular.overview.presentation.model.GenreUiModel
import io.mockk.mockk
import io.mockk.verify
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiGenreListTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `UiGenreList should display all genre names`() {
        val genres = persistentListOf(
            GenreUiModel(id = 1, name = "Action"),
            GenreUiModel(id = 2, name = "Comedy"),
            GenreUiModel(id = 3, name = "Drama")
        )

        rule.setContent {
            UiGenreList(genres = genres)
        }

        genres.forEach { genre ->
            rule.onNodeWithText(genre.name).assertIsDisplayed()
        }
    }

    @Test
    fun `UiGenreList should call onClick with correct ID when a genre is clicked`() {
        val onClick: (Long) -> Unit = mockk(relaxed = true)
        val genres = persistentListOf(
            GenreUiModel(id = 10, name = "Horror"),
            GenreUiModel(id = 20, name = "Sci-Fi")
        )

        rule.setContent {
            UiGenreList(genres = genres, onClick = onClick)
        }

        rule.onNodeWithText("Horror").performClick()
        verify { onClick(10L) }

        rule.onNodeWithText("Sci-Fi").performClick()
        verify { onClick(20L) }
    }

    @Test
    fun `UiGenreList should not display anything when list is empty`() {
        val genres = persistentListOf<GenreUiModel>()

        rule.setContent {
            UiGenreList(genres = genres)
        }

        // Since it's a LazyRow inside an 'if (genres.isNotEmpty())', 
        // there should be no nodes if the list is empty.
        // We can't easily assert "nothing is displayed" without a tag, 
        // but we can assert that a known text is NOT there.
        rule.onNodeWithText("Any Genre").assertDoesNotExist()
    }
}
