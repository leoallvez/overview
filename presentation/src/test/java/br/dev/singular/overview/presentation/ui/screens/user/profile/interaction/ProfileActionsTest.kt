package br.dev.singular.overview.presentation.ui.screens.user.profile.interaction

import br.dev.singular.overview.presentation.NavigationWrapperMock
import br.dev.singular.overview.presentation.tagging.TagManager
import br.dev.singular.overview.presentation.tagging.params.TagCommon
import br.dev.singular.overview.presentation.ui.screens.user.login.interaction.LoginIntent
import io.mockk.justRun
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ProfileActionsTest {

    private val tagPath = "/profile"
    private val navigation = NavigationWrapperMock()
    private val intents = mutableListOf<LoginIntent>()
    private val sut = ProfileActions(
        navigation = navigation,
        handleIntent = { intents.add(it) }
    )

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
    fun `onLogoutRequest should log click without signing out`() {
        // When
        sut.onLogoutRequest()

        // Then
        verify(exactly = 1) { TagManager.logClick(tagPath, "sign-out") }
        assertTrue(intents.isEmpty())
    }

    @Test
    fun `onLogout should log click and trigger the Logout intent`() {
        // When
        sut.onLogout()

        // Then
        verify(exactly = 1) { TagManager.logClick(tagPath, "sign-out-confirm") }
        assertEquals(listOf<LoginIntent>(LoginIntent.Logout), intents)
    }

    @Test
    fun `onLogoutCancel should log click without signing out`() {
        // When
        sut.onLogoutCancel()

        // Then
        verify(exactly = 1) { TagManager.logClick(tagPath, "sign-out-cancel") }
        assertTrue(intents.isEmpty())
    }

    @Test
    fun `onBack should log click and navigate back`() {
        // When
        sut.onBack()

        // Then
        verify(exactly = 1) { TagManager.logClick(tagPath, TagCommon.Detail.BACK) }
        assertTrue(navigation.wasPopBackStackCalled)
    }

    @Test
    fun `onSignedOut should navigate back without logging`() {
        // When
        sut.onSignedOut()

        // Then
        verify(exactly = 0) { TagManager.logClick(any(), any(), any()) }
        assertTrue(navigation.wasPopBackStackCalled)
        assertFalse(navigation.wasNavigateCalled)
    }
}
