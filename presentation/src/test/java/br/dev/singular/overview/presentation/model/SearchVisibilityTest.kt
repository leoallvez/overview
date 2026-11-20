package br.dev.singular.overview.presentation.model

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchVisibilityTest {

    @Test
    fun `shouldShowSearch should be true from the minimum of items`() {
        // act
        val result = shouldShowSearch(itemCount = 12, query = "")

        // assert
        assertTrue(result)
    }

    @Test
    fun `shouldShowSearch should be false below the minimum of items without query`() {
        // act
        val result = shouldShowSearch(itemCount = 11, query = "")

        // assert
        assertFalse(result)
    }

    @Test
    fun `shouldShowSearch should be true below the minimum of items while searching`() {
        // act
        val result = shouldShowSearch(itemCount = 0, query = "a")

        // assert
        assertTrue(result)
    }
}
