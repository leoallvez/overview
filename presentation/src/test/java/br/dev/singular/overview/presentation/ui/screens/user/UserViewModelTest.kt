package br.dev.singular.overview.presentation.ui.screens.user

import br.dev.singular.overview.domain.model.User
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.media.ISyncFavoritesUseCase
import br.dev.singular.overview.domain.usecase.user.IUserSessionUseCase
import br.dev.singular.overview.presentation.ActionState
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.createUserMock
import br.dev.singular.overview.presentation.ui.screens.common.UiEvent
import br.dev.singular.overview.presentation.ui.screens.common.UiEvents
import br.dev.singular.overview.presentation.ui.screens.user.login.interaction.LoginIntent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val useCase: IUserSessionUseCase = mockk()
    private val syncFavoritesUseCase: ISyncFavoritesUseCase = mockk()
    private val session = MutableStateFlow<User?>(null)

    private lateinit var sut: UserViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { useCase.observe() } returns session
        coEvery { syncFavoritesUseCase() } returns UseCaseState.Success(true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private val uiEvents: UiEvents = mockk(relaxed = true)

    private fun createSut() {
        sut = UserViewModel(
            dispatcher = testDispatcher,
            useCase = useCase,
            syncFavoritesUseCase = syncFavoritesUseCase,
            uiEvents = uiEvents
        )
    }

    @Test
    fun `uiState should start as Loading`() = runTest {
        // act
        createSut()

        // assert
        assertTrue(sut.uiState.value is UiState.Loading)
        assertEquals(ActionState.Idle, sut.actionState.value)
    }

    @Test
    fun `uiState should emit Success without user when there is no session`() = runTest {
        // act
        createSut()
        advanceUntilIdle()

        // assert
        val state = sut.uiState.value
        assertTrue(state is UiState.Success)
        assertNull((state as UiState.Success).data)
    }

    @Test
    fun `uiState should follow the session changes`() = runTest {
        // arrange
        val user = createUserMock()
        createSut()
        advanceUntilIdle()

        // act
        session.value = user
        advanceUntilIdle()

        // assert
        val signedIn = sut.uiState.value as UiState.Success
        assertEquals(user.id, signedIn.data?.id)
        assertEquals(user.photoUrl, signedIn.data?.photoURL)

        // act
        session.value = null
        advanceUntilIdle()

        // assert
        assertNull((sut.uiState.value as UiState.Success).data)
    }

    @Test
    fun `uiState should emit Error when observing the session fails`() = runTest {
        // arrange
        every { useCase.observe() } returns flow { throw IllegalStateException("error") }

        // act
        createSut()
        advanceUntilIdle()

        // assert
        assertTrue(sut.uiState.value is UiState.Error)
    }

    @Test
    fun `Login should set actionState to Loading`() = runTest {
        // arrange
        createSut()

        // act
        sut.handleIntent(LoginIntent.Login)

        // assert
        assertEquals(ActionState.Loading, sut.actionState.value)
    }

    @Test
    fun `Authenticate should set actionState to Success when sign in succeeds`() = runTest {
        // arrange
        coEvery { useCase.signIn("token") } returns UseCaseState.Success(createUserMock())
        createSut()

        // act
        sut.handleIntent(LoginIntent.Authenticate("token"))

        // assert
        assertEquals(ActionState.Loading, sut.actionState.value)
        advanceUntilIdle()
        assertEquals(ActionState.Success, sut.actionState.value)
        coVerify(exactly = 1) { useCase.signIn("token") }
        verify(exactly = 1) { uiEvents.trigger(UiEvent.ReloadFavorites) }
    }

    @Test
    fun `Authenticate should sync the favorites before reloading them`() = runTest {
        // arrange
        coEvery { useCase.signIn("token") } returns UseCaseState.Success(createUserMock())
        createSut()

        // act
        sut.handleIntent(LoginIntent.Authenticate("token"))
        advanceUntilIdle()

        // assert
        coVerifyOrder {
            syncFavoritesUseCase()
            uiEvents.trigger(UiEvent.ReloadFavorites)
        }
    }

    @Test
    fun `Authenticate should set actionState to Success when the favorites sync fails`() = runTest {
        // arrange
        coEvery { useCase.signIn("token") } returns UseCaseState.Success(createUserMock())
        coEvery { syncFavoritesUseCase() } returns UseCaseState.Failure(FailType.Invalid)
        createSut()

        // act
        sut.handleIntent(LoginIntent.Authenticate("token"))
        advanceUntilIdle()

        // assert
        assertEquals(ActionState.Success, sut.actionState.value)
    }

    @Test
    fun `Authenticate should set actionState to Error when sign in fails`() = runTest {
        // arrange
        coEvery { useCase.signIn(any()) } returns UseCaseState.Failure(FailType.Invalid)
        createSut()

        // act
        sut.handleIntent(LoginIntent.Authenticate("token"))
        advanceUntilIdle()

        // assert
        assertEquals(ActionState.Error, sut.actionState.value)
        coVerify(exactly = 0) { syncFavoritesUseCase() }
    }

    @Test
    fun `Fail should set actionState to Error`() = runTest {
        // arrange
        createSut()

        // act
        sut.handleIntent(LoginIntent.Fail)

        // assert
        assertEquals(ActionState.Error, sut.actionState.value)
    }

    @Test
    fun `Reset should set actionState to Idle`() = runTest {
        // arrange
        createSut()
        sut.handleIntent(LoginIntent.Fail)

        // act
        sut.handleIntent(LoginIntent.Reset)

        // assert
        assertEquals(ActionState.Idle, sut.actionState.value)
    }

    @Test
    fun `Logout should set actionState to Success when sign out succeeds`() = runTest {
        // arrange
        coEvery { useCase.signOut() } returns UseCaseState.Success(Unit)
        createSut()

        // act
        sut.handleIntent(LoginIntent.Logout)

        // assert
        assertEquals(ActionState.Loading, sut.actionState.value)
        advanceUntilIdle()
        assertEquals(ActionState.Success, sut.actionState.value)
        coVerify(exactly = 1) { useCase.signOut() }
    }

    @Test
    fun `Logout should set actionState to Error when sign out fails`() = runTest {
        // arrange
        coEvery { useCase.signOut() } returns UseCaseState.Failure(FailType.Invalid)
        createSut()

        // act
        sut.handleIntent(LoginIntent.Logout)
        advanceUntilIdle()

        // assert
        assertEquals(ActionState.Error, sut.actionState.value)
    }
}
