package br.dev.singular.overview.presentation.ui.screens.catalog.details.interaction

import br.dev.singular.overview.presentation.tagging.TagManager
import io.mockk.justRun
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CatalogDetailActionsTest {

    private val intents = mutableListOf<CatalogDetailIntent>()
    private val sut = CatalogDetailActions(handleIntent = { intents.add(it) })

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
        sut.onSearch("batman")

        // assert
        verify(exactly = 1) { TagManager.logInteraction(sut.tagPath, "search-field") }
        assertEquals(listOf(CatalogDetailIntent.Search("batman")), intents)
    }

    @Test
    fun `onClear should log click`() {
        // act
        sut.onClear()

        // assert
        verify(exactly = 1) { TagManager.logClick(sut.tagPath, "clean-search-field") }
    }

    @Test
    fun `onToggleSearch should log click without sending intent when opening`() {
        // act
        sut.onToggleSearch(isOpen = true)

        // assert
        verify(exactly = 1) { TagManager.logClick(sut.tagPath, "open-search") }
        assertEquals(emptyList<CatalogDetailIntent>(), intents)
    }

    @Test
    fun `onToggleSearch should log click and clear the search when closing`() {
        // act
        sut.onToggleSearch(isOpen = false)

        // assert
        verify(exactly = 1) { TagManager.logClick(sut.tagPath, "close-search") }
        assertEquals(listOf(CatalogDetailIntent.Search("")), intents)
    }
}
