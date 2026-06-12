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
import kotlinx.coroutines.runBlocking
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
    fun `invoke should return success with media page`() = runBlocking {
        // arrange
        coEvery { getterPageMock.getPage(any()) } returns
                Page(items = listOf(createMediaMock(), createMediaMock()))

        // act
        val result = sut.invoke(QueryState())

        // assert
        coVerify(exactly = 1) { getterPageMock.getPage(any()) }
        assertTrue(result is UseCaseState.Success)
        assertEquals(2, (result as UseCaseState.Success).data.items.size)
    }

    @Test
    fun `invoke should return Failure when getter throws exception`() = runBlocking {
        // Arrange
        val expectedException = Exception("Getter failed")
        coEvery { getterPageMock.getPage(any()) } throws expectedException

        // Act
        val result = sut.invoke(QueryState())

        // Assert
        coVerify(exactly = 1) { getterPageMock.getPage(any()) }

        assertTrue(result is UseCaseState.Failure)
        val failure = result as UseCaseState.Failure
        assertEquals(
            expectedException,
            (failure.type as FailType.Exception).throwable
        )
    }
}
