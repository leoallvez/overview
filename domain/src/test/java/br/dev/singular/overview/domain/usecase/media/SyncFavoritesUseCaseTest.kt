package br.dev.singular.overview.domain.usecase.media

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.User
import br.dev.singular.overview.domain.repository.Get
import br.dev.singular.overview.domain.repository.GetAll
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

class SyncFavoritesUseCaseTest {

    private lateinit var sut: ISyncFavoritesUseCase
    private val session: Get<User?> = mockk()
    private val remote: GetAll<Media> = mockk()
    private val local: GetAll<Media> = mockk()
    private val saver: Update<List<Media>> = mockk(relaxed = true)

    @Before
    fun setup() {
        sut = SyncFavoritesUseCase(session, remote, local, saver)
        coEvery { session.get() } returns createUserMock()
    }

    @Test
    fun `invoke should like the remote favorites missing locally`() = runTest {
        // arrange
        val both = createMediaMock(isLiked = true).copy(id = 1)
        val onlyRemote = createMediaMock(isLiked = true).copy(id = 2)
        coEvery { remote.getAll() } returns listOf(both, onlyRemote)
        coEvery { local.getAll() } returns listOf(both)

        // act
        val result = sut()

        // assert
        assertEquals(UseCaseState.Success(true), result)
        coVerify(exactly = 1) { saver.update(listOf(onlyRemote)) }
    }

    @Test
    fun `invoke should unlike the local favorites removed remotely`() = runTest {
        // arrange
        val both = createMediaMock(isLiked = true).copy(id = 1)
        val onlyLocal = createMediaMock(isLiked = true).copy(id = 2)
        coEvery { remote.getAll() } returns listOf(both)
        coEvery { local.getAll() } returns listOf(both, onlyLocal)

        // act
        val result = sut()

        // assert
        assertEquals(UseCaseState.Success(true), result)
        coVerify(exactly = 1) { saver.update(listOf(onlyLocal.copy(isLiked = false))) }
    }

    @Test
    fun `invoke should compare the favorites by type and id`() = runTest {
        // arrange
        val movie = createMediaMock(isLiked = true, type = MediaType.MOVIE)
        val tvShow = createMediaMock(isLiked = true, type = MediaType.TV)
        coEvery { remote.getAll() } returns listOf(movie)
        coEvery { local.getAll() } returns listOf(tvShow)

        // act
        val result = sut()

        // assert
        assertEquals(UseCaseState.Success(true), result)
        coVerify(exactly = 1) { saver.update(listOf(tvShow.copy(isLiked = false), movie)) }
    }

    @Test
    fun `invoke should save nothing when the favorites are already in sync`() = runTest {
        // arrange
        val media = createMediaMock(isLiked = true)
        coEvery { remote.getAll() } returns listOf(media)
        coEvery { local.getAll() } returns listOf(media)

        // act
        val result = sut()

        // assert
        assertEquals(UseCaseState.Success(false), result)
        coVerify(exactly = 0) { saver.update(any()) }
    }

    @Test
    fun `invoke should return Success without syncing when there is no session`() = runTest {
        // arrange
        coEvery { session.get() } returns null

        // act
        val result = sut()

        // assert
        assertEquals(UseCaseState.Success(false), result)
        coVerify(exactly = 0) { remote.getAll() }
        coVerify(exactly = 0) { saver.update(any()) }
    }

    @Test
    fun `invoke should return Exception failure and keep the local favorites when remote fails`() = runTest {
        // arrange
        val exception = RuntimeException("remote error")
        coEvery { remote.getAll() } throws exception
        coEvery { local.getAll() } returns listOf(createMediaMock(isLiked = true))

        // act
        val result = sut()

        // assert
        assertEquals(UseCaseState.Failure(FailType.Exception(exception)), result)
        coVerify(exactly = 0) { saver.update(any()) }
    }
}
