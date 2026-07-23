package br.dev.singular.overview.data.network.source

import br.dev.singular.overview.data.model.MediaDataPage
import br.dev.singular.overview.data.model.QueryDataState
import br.dev.singular.overview.data.network.ApiService
import com.haroldadmin.cnradapter.NetworkResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

class MediaRemoteDataSourceTest {

    private val api: ApiService = mockk()
    private val sut = MediaRemoteDataSource(api)

    @Test
    fun `getByQuery should return Success when API returns success`() = runTest {
        // arrange
        val queryState = QueryDataState(path = "movie/popular", page = 1)
        val page = MediaDataPage(page = 1, items = emptyList())
        coEvery { 
            api.fetchMediaPage(
                path = any(),
                page = any(),
                query = any(),
                options = any()
            ) 
        } returns NetworkResponse.Success(page, mockk(), 200)

        // act
        val result = sut.getByQuery(queryState, emptyMap())

        // assert
        assertTrue(result is DataResult.Success)
        assertEquals(page, (result as DataResult.Success).data)
    }

    @Test
    fun `getByQuery should return Error when API returns error`() = runTest {
        // arrange
        val queryState = QueryDataState(path = "movie/popular")
        coEvery { 
            api.fetchMediaPage(
                path = any(),
                page = any(),
                query = any(),
                options = any()
            ) 
        } returns NetworkResponse.UnknownError(Exception(), mockk())

        // act
        val result = sut.getByQuery(queryState, emptyMap())

        // assert
        assertTrue(result is DataResult.Error)
    }
}

private fun assertEquals(expected: Any?, actual: Any?) {
    org.junit.Assert.assertEquals(expected, actual)
}
