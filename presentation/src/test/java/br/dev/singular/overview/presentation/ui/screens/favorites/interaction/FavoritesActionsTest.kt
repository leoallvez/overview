package br.dev.singular.overview.presentation.ui.screens.favorites.interaction

import br.dev.singular.overview.presentation.NavigationWrapperMock
import br.dev.singular.overview.presentation.tagging.TagManager
import io.mockk.justRun
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FavoritesActionsTest {

    private val navigation = NavigationWrapperMock()
    private val sut = FavoritesActions(navigation = navigation)

    @Before
    fun setup() {
        mockkObject(TagManager)
        justRun { TagManager.logClick(any(), any(), any()) }
    }

    @After
    fun tearDown() {
        unmockkObject(TagManager)
    }

    @Test
    fun `onToProfile should log click and navigate to the profile`() {
        // When
        sut.onToProfile()

        // Then
        verify(exactly = 1) { TagManager.logClick(sut.tagPath, "profile") }
        assertTrue(navigation.wasNavigateCalled)
    }
}
