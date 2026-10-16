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

    @Test
    fun `extractPath should return the path used to build the image URL`() {
        val path = "/logo.jpg"
        assertEquals(path, extractPath(buildImageFullURL(path)))
    }

    @Test
    fun `extractPath should return the path when the base URL is repeated`() {
        val path = "/logo.jpg"
        val url = buildImageFullURL(buildImageFullURL(path))
        assertEquals(path, extractPath(url))
    }

    @Test
    fun `extractPath should return the URL when it has no base URL`() {
        val url = "https://example.com/logo.png"
        assertEquals(url, extractPath(url))
    }

    @Test
    fun `extractPath should return empty when the URL is blank`() {
        assertEquals("", extractPath(" "))
    }
}
