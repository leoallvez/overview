package br.dev.singular.overview.domain.usecase

import br.dev.singular.overview.domain.model.Person
import br.dev.singular.overview.domain.repository.GetById
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetPersonByIdUseCaseTest {

    private lateinit var sut: IGetPersonByIdUseCase

    lateinit var getter: GetById<Person>

    @Before
    fun setup() {
        getter = mockk()
        sut = GetPersonByIdUseCase(getter)
    }

    @Test
    fun `invoke should return success with person`() = runBlocking {
        // arrange
        coEvery { getter.getById(any()) } returns createPersonMock()

        // act
        val result = sut.invoke(0)

        // assert
        coVerify(exactly = 1) { getter.getById(any()) }
        assertTrue(result is UseCaseState.Success)
        assertNotNull((result as UseCaseState.Success).data)
    }

    @Test
    fun `invoke should return success with null`() = runBlocking {
        // arrange
        coEvery { getter.getById(any()) } returns null

        // act
        val result = sut.invoke(0)

        // assert
        coVerify(exactly = 1) { getter.getById(any()) }
        assertTrue(result is UseCaseState.Success)
        assertNull((result as UseCaseState.Success).data)
    }

    @Test
    fun `invoke should return Failure when getter throws exception`() = runBlocking {
        // Arrange
        val expectedException = Exception("Getter failed")
        coEvery { getter.getById(any()) } throws expectedException

        // Act
        val result = sut.invoke(0)

        // Assert
        coVerify(exactly = 1) { getter.getById(any()) }

        assertTrue(result is UseCaseState.Failure)
        val failure = result as UseCaseState.Failure
        assertEquals(
            expectedException,
            (failure.type as FailType.Exception).throwable
        )
    }
}
