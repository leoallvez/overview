package br.dev.singular.overview.presentation.ui.screens.episodes.interaction

import br.dev.singular.overview.presentation.NavigationWrapperMock
import br.dev.singular.overview.presentation.tagging.TagManager
import br.dev.singular.overview.presentation.tagging.params.TagCommon
import io.mockk.justRun
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class EpisodesActionsTest {

    private val navigation = NavigationWrapperMock()
    private val sut = EpisodesActions(navigation = navigation)

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
    fun `onBack should log click and pop the back stack`() {
        // act
        sut.onBack()

        // assert
        verify(exactly = 1) { TagManager.logClick(sut.tagPath, TagCommon.Detail.BACK) }
        assertTrue(navigation.wasPopBackStackCalled)
    }
}
