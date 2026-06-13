package br.dev.singular.overview.presentation.ui.components.style

import androidx.compose.ui.layout.ContentScale
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class UiImageStyleTest {

    @Test
    fun `UiImageStyle default values`() {
        val style = UiImageStyle()
        assertEquals(ContentScale.FillHeight, style.contentScale)
        assertFalse(style.borderStyle.visible)
    }
}
