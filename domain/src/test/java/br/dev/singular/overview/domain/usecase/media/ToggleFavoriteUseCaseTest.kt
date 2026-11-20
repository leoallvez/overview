package br.dev.singular.overview.domain.usecase.media

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.User
import br.dev.singular.overview.domain.repository.Get
import br.dev.singular.overview.domain.repository.Update
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.createMediaMock
import br.dev.singular.overview.domain.usecase.createUserMock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ToggleFavoriteUseCaseTest {

    private lateinit var sut: IToggleFavoriteUseCase
    private val session: Get<User?> = mockk()
    private val updater: Update<Media> = mockk(relaxed = true)

    @Before
    fun setup() {
        sut = ToggleFavoriteUseCase(session, updater)
    }

    @Test
    fun `invoke should like the media when the user is signed in`() = runTest {
        // arrange
        val media = createMediaMock(isLiked = false)
        coEvery { session.get() } returns createUserMock()

        // act
        val result = sut(media)

        // assert
        assertEquals(UseCaseState.Success(true), result)
        coVerify(exactly = 1) { updater.update(media.copy(isLiked = true)) }
    }

    @Test
    fun `invoke should unlike the media when it is already liked`() = runTest {
        // arrange
        val media = createMediaMock(isLiked = true)
        coEvery { session.get() } returns createUserMock()

        // act
        val result = sut(media)

        // assert
        assertEquals(UseCaseState.Success(false), result)
        coVerify(exactly = 1) { updater.update(media.copy(isLiked = false)) }
    }

    @Test
    fun `invoke should return Unauthorized failure when there is no session`() = runTest {
        // arrange
        coEvery { session.get() } returns null

        // act
        val result = sut(createMediaMock())

        // assert
        assertEquals(UseCaseState.Failure(FailType.Unauthorized), result)
        coVerify(exactly = 0) { updater.update(any()) }
    }

    @Test
    fun `invoke should return Unauthorized failure when the session fails`() = runTest {
        // arrange
        coEvery { session.get() } throws RuntimeException("session error")

        // act
        val result = sut(createMediaMock())

        // assert
        assertEquals(UseCaseState.Failure(FailType.Unauthorized), result)
        coVerify(exactly = 0) { updater.update(any()) }
    }

    @Test
    fun `invoke should return Exception failure when repository fails`() = runTest {
        // arrange
        val exception = RuntimeException("update error")
        coEvery { session.get() } returns createUserMock()
        coEvery { updater.update(any()) } throws exception

        // act
        val result = sut(createMediaMock())

        // assert
        assertEquals(UseCaseState.Failure(FailType.Exception(exception)), result)
    }
}
