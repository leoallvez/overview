package br.dev.singular.overview.presentation.ui.screens.media.movie.interaction

import br.dev.singular.overview.presentation.NavigationWrapperMock
import br.dev.singular.overview.presentation.tagging.TagManager
import br.dev.singular.overview.presentation.ui.components.snackbar.UiSnackbarVisuals
import br.dev.singular.overview.presentation.ui.navigation.Destination
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

class MovieDetailsActionsTest {

    private val navigation = NavigationWrapperMock()
    private val intents = mutableListOf<MovieDetailsIntent>()
    private val snackbars = mutableListOf<UiSnackbarVisuals>()
    private val sut = MovieDetailsActions(
        navigation = navigation,
        handleIntent = { intents.add(it) },
        onShowSnackbar = { snackbars.add(it) }
    )

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
    fun `onFavoriteAdded should log interaction, show the snackbar and dismiss the notice`() {
        // Given
        val visuals: UiSnackbarVisuals = mockk()

        // When
        sut.onFavoriteAdded(visuals)

        // Then
        verify(exactly = 1) { TagManager.logInteraction(sut.tagPath, "favorite-added") }
        assertEquals(listOf(visuals), snackbars)
        assertEquals(listOf<MovieDetailsIntent>(MovieDetailsIntent.DismissFavoriteAdded), intents)
    }

    @Test
    fun `onLoginConfirm should dismiss the alert, log click and navigate to the login`() {
        // When
        sut.onLoginConfirm()

        // Then
        assertEquals(listOf<MovieDetailsIntent>(MovieDetailsIntent.DismissLoginAlert), intents)
        verify(exactly = 1) { TagManager.logClick(sut.tagPath, "login-required-confirm") }
        assertEquals(Destination.Favorites.route, navigation.activeRoute)
    }

    @Test
    fun `onLoginDismiss should dismiss the alert and log click without navigating`() {
        // When
        sut.onLoginDismiss()

        // Then
        assertEquals(listOf<MovieDetailsIntent>(MovieDetailsIntent.DismissLoginAlert), intents)
        verify(exactly = 1) { TagManager.logClick(sut.tagPath, "login-required-cancel") }
        assertFalse(navigation.wasNavigateCalled)
    }
}
