package br.dev.singular.overview.presentation.ui.screens.media

import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.snackbar.UiSnackbarVisuals
import com.composables.icons.lucide.Heart
import com.composables.icons.lucide.Lucide
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class MediaFavoriteAddedEffectTest {

    @get:Rule
    val rule = createComposeRule()

    private val context = RuntimeEnvironment.getApplication()
    private val snackbars = mutableListOf<UiSnackbarVisuals>()

    @Test
    fun `MediaFavoriteAddedEffect should show the snackbar when the favorite is added`() {
        rule.setContent {
            MediaFavoriteAddedEffect(
                favoriteAdded = true,
                mediaTitle = "Interstellar",
                onShow = { snackbars.add(it) }
            )
        }
        rule.waitForIdle()

        assertEquals(1, snackbars.size)
        val visuals = snackbars.first()
        assertTrue(visuals is UiSnackbarVisuals.Close)
        assertEquals(context.getString(R.string.favorite_added_title), visuals.title)
        assertEquals("Interstellar", visuals.message)
        assertEquals(UiIconSource.vector(Lucide.Heart), visuals.icon)
    }

    @Test
    fun `MediaFavoriteAddedEffect should not show the snackbar when no favorite is added`() {
        rule.setContent {
            MediaFavoriteAddedEffect(
                favoriteAdded = false,
                mediaTitle = "Interstellar",
                onShow = { snackbars.add(it) }
            )
        }
        rule.waitForIdle()

        assertTrue(snackbars.isEmpty())
    }
}
