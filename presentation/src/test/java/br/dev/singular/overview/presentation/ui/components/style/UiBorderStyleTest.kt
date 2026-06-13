package br.dev.singular.overview.presentation.ui.components.style

import androidx.compose.ui.graphics.Color
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class UiBorderStyleTest {

    @Test
    fun `UiBorderStyle default values`() {
        val style = UiBorderStyle()
        assertTrue(style.visible)
    }

    @Test
    fun `UiBorderStyle explicit values`() {
        val style = UiBorderStyle(visible = false, color = Color.Red)
        assertFalse(style.visible)
        assertEquals(Color.Red, style.color)
    }
}
