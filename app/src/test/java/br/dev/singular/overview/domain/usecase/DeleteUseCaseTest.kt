package br.dev.singular.overview.domain.usecase

import br.dev.singular.overview.domain.repository.Delete
import br.dev.singular.overview.domain.repository.GetAll
import io.mockk.coEvery
import io.mockk.coVerify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test

abstract class DeleteUseCaseTest <T> {

    protected lateinit var sut: IDeleteUseCase
    protected abstract var getter: GetAll<T>
    protected abstract var deleter: Delete<T>

    protected abstract fun createMockToKeep(): T
    protected abstract fun createMockToDelete(): T

    @Test
    fun `invoke should return Success(true) when there are models to delete`() = runTest {
        // Arrange
        val toDelete = createMockToDelete()
        val toKeep = createMockToKeep()
        coEvery { getter.getAll() } returns listOf(toDelete, toKeep)
        coEvery { deleter.delete(any()) } returns Unit

        // Act
        val result = sut.invoke()

        // Assert
        coVerify(exactly = 1) { deleter.delete(toDelete) }
        coVerify(exactly = 0) { deleter.delete(toKeep) }
        assertEquals(UseCaseState.Success(true), result)
    }

    @Test
    fun `invoke should return Success(false) when there are no models to delete`() = runTest {
        // Arrange
        coEvery { getter.getAll() } returns emptyList()
        coEvery { deleter.delete() } returns Unit

        // Act
        val result = sut.invoke()

        // Assert
        coVerify(exactly = 0) { deleter.delete(any()) }
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

    @Test
    fun `should return Failure when deleter throws an exception`() = runTest {
        val toDelete = createMockToDelete()
        coEvery { getter.getAll() } returns listOf(toDelete)
        coEvery { deleter.delete(any()) } throws Exception("DB error")

        val result = sut.invoke()

        coVerify(exactly = 1) { deleter.delete(toDelete) }
        assertTrue(result is UseCaseState.Failure)
        assertTrue((result as UseCaseState.Failure).type is FailType.Exception)
    }
}