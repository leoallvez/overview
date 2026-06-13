package br.dev.singular.overview.domain.usecase.media

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.QueryState
import br.dev.singular.overview.domain.repository.GetPage
import br.dev.singular.overview.domain.repository.Page
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.createMediaMock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetAllLocalMediasUseCaseTest {

    private lateinit var sut: IGetAllLocalMediasUseCase
    private lateinit var getterPageMock: GetPage<Media, QueryState>

    @Before
    fun setup() {
        getterPageMock = mockk()
        sut = GetAllLocalMediasUseCase(getterPageMock)
    }

    @Test
    fun `invoke should return success with media page`() = runTest {
        // arrange
        val query = QueryState(query = "local")
        val expectedPage = Page(items = listOf(createMediaMock(), createMediaMock()))
        coEvery { getterPageMock.getPage(query) } returns expectedPage

        // act
        val result = sut.invoke(query)

        // assert
        coVerify(exactly = 1) { getterPageMock.getPage(query) }
        assertEquals(UseCaseState.Success(expectedPage), result)
    }

    @Test
    fun `invoke should return Failure when getter throws exception`() = runTest {
        // Arrange
        val expectedException = RuntimeException("Local DB Error")
        coEvery { getterPageMock.getPage(any()) } throws expectedException

        // Act
        val result = sut.invoke(QueryState())

        // Assert
        assertTrue(result is UseCaseState.Failure)
        val failure = result as UseCaseState.Failure
        assertEquals(expectedException, (failure.type as FailType.Exception).throwable)
    }
}
