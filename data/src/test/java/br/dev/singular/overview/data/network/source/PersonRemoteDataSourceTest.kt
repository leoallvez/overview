package br.dev.singular.overview.data.network.source

import br.dev.singular.overview.data.model.PersonDataModel
import br.dev.singular.overview.data.network.ApiService
import com.haroldadmin.cnradapter.NetworkResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

class PersonRemoteDataSourceTest {

    private val api: ApiService = mockk()
    private val sut = PersonRemoteDataSource(api)

    @Test
    fun `getById should return Success when API returns success`() = runTest {
        // arrange
        val person = PersonDataModel(id = 1, name = "John Doe")
        coEvery { api.getPersonById(any()) } returns NetworkResponse.Success(person, mockk(), 200)

        // act
        val result = sut.getById(1L)

        // assert
        assertTrue(result is DataResult.Success)
        assertEquals(person, (result as DataResult.Success).data)
    }

    @Test
    fun `getById should return Error when API returns error`() = runTest {
        // arrange
        coEvery { api.getPersonById(any()) } returns NetworkResponse.UnknownError(Exception(), mockk())

        // act
        val result = sut.getById(1L)

        // assert
        assertTrue(result is DataResult.Error)
    }
}

private fun assertEquals(expected: Any?, actual: Any?) {
    org.junit.Assert.assertEquals(expected, actual)
}
