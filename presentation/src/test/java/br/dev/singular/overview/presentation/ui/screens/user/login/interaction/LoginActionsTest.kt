package br.dev.singular.overview.presentation.ui.screens.user.login.interaction

import br.dev.singular.overview.presentation.tagging.TagManager
import br.dev.singular.overview.presentation.ui.components.snackbar.UiSnackbarVisuals
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LoginActionsTest {

    private val tagPath = "/login"
    private val intents = mutableListOf<LoginIntent>()
    private val errors = mutableListOf<UiSnackbarVisuals>()
    private var idTokenRequests = 0

    private val sut = LoginActions(
        handleIntent = { intents.add(it) },
        onRequestIdToken = { idTokenRequests++ },
        onShowSnackbar = { errors.add(it) }
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
    fun `onLogin should log click, trigger the Login intent and request the id token`() {
        // When
        sut.onLogin()

        // Then
        verify(exactly = 1) { TagManager.logClick(tagPath, "sign-in-with-google") }
        assertEquals(listOf<LoginIntent>(LoginIntent.Login), intents)
        assertEquals(1, idTokenRequests)
    }

    @Test
    fun `onError should log interaction, show the error and trigger the Reset intent`() {
        // Given
        val visuals: UiSnackbarVisuals = mockk()

        // When
        sut.onError(visuals)

        // Then
        verify(exactly = 1) { TagManager.logInteraction(tagPath, "sign-in-error") }
        assertEquals(listOf(visuals), errors)
        assertEquals(listOf<LoginIntent>(LoginIntent.Reset), intents)
    }
}
