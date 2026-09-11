package br.dev.singular.overview.domain.usecase.catalog

import br.dev.singular.overview.domain.model.Catalog
import br.dev.singular.overview.domain.repository.GetAll
import br.dev.singular.overview.domain.usecase.createCatalogMock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetAllCatalogUseCaseTest {
    private lateinit var getter: GetAll<Catalog>
    private lateinit var sut: IGetAllCatalogUseCase
    private lateinit var catalogMock: Catalog

    @Before
    fun setup() {
        getter = mockk()
        sut = GetAllCatalogUseCase(getter)
        catalogMock = createCatalogMock()
    }

    @Test
    fun `invoke should return only catalog where display is true`() = runBlocking {
        // arrange
        val list = listOf(
            catalogMock.copy(id = 1, display = true),
            catalogMock.copy(id = 2, display = false),
            catalogMock.copy(id = 3, display = true)
        )
        coEvery { getter.getAll() } returns list

        // act
        val result = sut.invoke()

        // assert
        coVerify(exactly = 1) { getter.getAll() }
        assertEquals(2, result.size)
        assertTrue(result.all { it.display })
    }

    @Test
    fun `invoke should return empty list when no catalog has display true`() = runBlocking {
        // arrange
        val list = listOf(
            catalogMock.copy(display = false),
            catalogMock.copy(display = false)
        )
        coEvery { getter.getAll() } returns list

        // act
        val result = sut.invoke()

        // assert
        assertTrue(result.isEmpty())
    }

    @Test
    fun `invoke should return empty list when repository throws exception`() = runBlocking {
        // arrange
        coEvery { getter.getAll() } throws Exception("Network Error")

        // act
        val result = sut.invoke()

        // assert
        coVerify(exactly = 1) { getter.getAll() }
        assertTrue(result.isEmpty())
    }

    @Test
    fun `invoke should return empty list when repository returns empty`() = runBlocking {
        // arrange
        coEvery { getter.getAll() } returns emptyList()

        // act
        val result = sut.invoke()

        // assert
        assertTrue(result.isEmpty())
    }
}
