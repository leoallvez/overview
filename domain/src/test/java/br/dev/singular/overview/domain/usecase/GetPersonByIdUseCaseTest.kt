package br.dev.singular.overview.domain.usecase

import br.dev.singular.overview.domain.model.Person
import br.dev.singular.overview.domain.repository.GetById
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetPersonByIdUseCaseTest {

    private lateinit var sut: IGetPersonByIdUseCase

    private lateinit var getter: GetById<Person>

    @Before
    fun setup() {
        getter = mockk()
        sut = GetPersonByIdUseCase(getter)
    }

    @Test
    fun `invoke should return success with person`() = runTest {
        // arrange
        val person = createPersonMock()
        coEvery { getter.getById(1L) } returns person

        // act
        val result = sut.invoke(1L)

        // assert
        coVerify(exactly = 1) { getter.getById(1L) }
        assertEquals(UseCaseState.Success(person), result)
    }

    @Test
    fun `invoke should return success with null`() = runTest {
        // arrange
        coEvery { getter.getById(1L) } returns null

        // act
        val result = sut.invoke(1L)

        // assert
        coVerify(exactly = 1) { getter.getById(1L) }
        assertEquals(UseCaseState.Success(null), result)
    }

    @Test
    fun `invoke should return Failure when getter throws exception`() = runTest {
        // Arrange
        val expectedException = RuntimeException("Getter failed")
        coEvery { getter.getById(any()) } throws expectedException

        // Act
        val result = sut.invoke(0)

        // Assert
        assertTrue(result is UseCaseState.Failure)
        val failure = result as UseCaseState.Failure
        assertEquals(expectedException, (failure.type as FailType.Exception).throwable)
    }
}
