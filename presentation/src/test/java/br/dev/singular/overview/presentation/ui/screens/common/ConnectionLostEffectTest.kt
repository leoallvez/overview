package br.dev.singular.overview.presentation.ui.screens.common

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.singular.overview.presentation.R
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.snackbar.UiSnackbarVisuals
import br.dev.singular.overview.presentation.ui.components.snackbar.style.UiSnackbarStyle
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.WifiOff
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class ConnectionLostEffectTest {

    @get:Rule
    val rule = createComposeRule()

    private val context = RuntimeEnvironment.getApplication()
    private val snackbars = mutableListOf<UiSnackbarVisuals>()

    @Test
    fun `ConnectionLostEffect should show the error snackbar when the device is offline`() {
        rule.setContent {
            ConnectionLostEffect(
                isOnline = false,
                onShow = { snackbars.add(it) },
                onDismiss = { snackbars.remove(it) }
            )
        }
        rule.waitForIdle()

        assertEquals(1, snackbars.size)
        val visuals = snackbars.first()
        assertTrue(visuals is UiSnackbarVisuals.Persistent)
        assertEquals(context.getString(R.string.connection_lost_title), visuals.title)
        assertEquals(context.getString(R.string.connection_lost_message), visuals.message)
        assertEquals(UiSnackbarStyle.Error, visuals.style)
        assertEquals(UiSnackbarVisuals.INDEFINITE_DURATION_MILLIS, visuals.durationMillis)
        assertEquals(UiIconSource.vector(Lucide.WifiOff), visuals.icon)
    }

    @Test
    fun `ConnectionLostEffect should not show the snackbar when the device is online`() {
        rule.setContent {
            ConnectionLostEffect(
                isOnline = true,
                onShow = { snackbars.add(it) },
                onDismiss = { snackbars.remove(it) }
            )
        }
        rule.waitForIdle()

        assertTrue(snackbars.isEmpty())
    }

    @Test
    fun `ConnectionLostEffect should dismiss the snackbar when the connection is back`() {
        var isOnline by mutableStateOf(false)
        rule.setContent {
            ConnectionLostEffect(
                isOnline = isOnline,
                onShow = { snackbars.add(it) },
                onDismiss = { snackbars.remove(it) }
            )
        }
        rule.waitForIdle()

        isOnline = true
        rule.waitForIdle()

        assertTrue(snackbars.isEmpty())
    }

    @Test
    fun `ConnectionLostEffect should show the snackbar again when the connection is lost again`() {
        var isOnline by mutableStateOf(false)
        rule.setContent {
            ConnectionLostEffect(
                isOnline = isOnline,
                onShow = { snackbars.add(it) },
                onDismiss = { snackbars.remove(it) }
            )
        }
        rule.waitForIdle()

        isOnline = true
        rule.waitForIdle()
        isOnline = false
        rule.waitForIdle()

        assertEquals(1, snackbars.size)
    }
}
