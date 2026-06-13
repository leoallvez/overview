package br.dev.singular.overview.presentation.ui.utils.mappers

import br.dev.singular.overview.presentation.BuildConfig
import org.junit.Assert.assertEquals
import org.junit.Test

class MappersUtilsTest {

    @Test
    fun `buildImageFullURL should concatenate base URL and path`() {
        val path = "/test.jpg"
        val expected = "${BuildConfig.IMG_URL}$path"
        assertEquals(expected, buildImageFullURL(path))
    }

    @Test
    fun `buildPosterURL should concatenate base URL and path`() {
        val path = "/poster.jpg"
        val expected = "${BuildConfig.POSTER_URL}$path"
        assertEquals(expected, buildPosterURL(path))
    }
}
