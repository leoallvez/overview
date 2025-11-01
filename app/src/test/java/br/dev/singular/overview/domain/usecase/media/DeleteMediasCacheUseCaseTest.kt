package br.dev.singular.overview.domain.usecase.media

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.repository.Delete
import br.dev.singular.overview.domain.repository.GetAll
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.createMediaMock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeleteMediasCacheUseCaseTest {

    private lateinit var sut: IDeleteMediasCacheUseCase
    private lateinit var getter: GetAll<Media>
    private lateinit var deleter: Delete<Media>

    @Before
    fun setup() {
        getter = mockk()
        deleter = mockk()
        sut = DeleteMediasCacheUseCase(getter, deleter, isTestMode = true)
    }

    @Test
    fun `invoke should return Success(true) when there are medias to delete`() = runTest {
        // Arrange
        val mediaToDelete = createMediaMock(isLiked = false)
        val mediaToKeep = createMediaMock(isLiked = true)
        coEvery { getter.getAll() } returns listOf(mediaToDelete, mediaToKeep)
        coEvery { deleter.delete(any()) } returns Unit

        // Act
        val result = sut.invoke()

        // Assert
        coVerify(exactly = 1) { deleter.delete(mediaToDelete) }
        coVerify(exactly = 0) { deleter.delete(mediaToKeep) }
        assertEquals(UseCaseState.Success(true), result)
    }

    @Test
    fun `invoke should return Success(false) when there are no medias to delete`() = runTest {
        // Arrange
        coEvery { getter.getAll() } returns emptyList()
        coEvery { deleter.delete() } returns Unit

        // Act
        val result = sut.invoke()

        // Assert
        coVerify(exactly = 1) { deleter.delete() }
        assertEquals(UseCaseState.Success(false), result)
    }

    @Test
    fun `invoke should return Failure when getter throws an exception`() = runTest {
        // Arrange
        coEvery { getter.getAll() } throws Exception("Unexpected error")

        // Act
        val result = sut.invoke()

        // Assert
        coVerify(exactly = 1) { getter.getAll() }
        assertTrue(result is UseCaseState.Failure)
        assertTrue((result as UseCaseState.Failure).type is FailType.Exception)
    }
}
