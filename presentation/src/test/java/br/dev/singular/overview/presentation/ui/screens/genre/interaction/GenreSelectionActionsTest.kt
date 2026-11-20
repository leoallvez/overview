package br.dev.singular.overview.presentation.ui.screens.genre.interaction

import br.dev.singular.overview.presentation.tagging.TagManager
import io.mockk.justRun
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GenreSelectionActionsTest {

    private val intents = mutableListOf<GenreSelectionIntent>()
    private val sut = GenreSelectionActions(handleIntent = { intents.add(it) })

    @Before
    fun setup() {
        mockkObject(TagManager)
        justRun { TagManager.logClick(any(), any(), any()) }
        justRun { TagManager.logInteraction(any(), any()) }
    }

    @After
    fun tearDown() {
        unmockkObject(TagManager)
    }

    @Test
    fun `onSearch should log interaction and send the search intent`() {
        // act
        sut.onSearch("drama")

        // assert
        verify(exactly = 1) { TagManager.logInteraction(sut.tagPath, "search-field") }
        assertEquals(listOf(GenreSelectionIntent.Search("drama")), intents)
    }

    @Test
    fun `onClear should log click without sending intent`() {
        // act
        sut.onClear()

        // assert
        verify(exactly = 1) { TagManager.logClick(sut.tagPath, "clean-search-field") }
        assertEquals(emptyList<GenreSelectionIntent>(), intents)
    }
}
