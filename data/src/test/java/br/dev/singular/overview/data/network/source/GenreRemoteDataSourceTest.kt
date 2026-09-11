package br.dev.singular.overview.data.network.source

import br.dev.singular.overview.data.model.GenreDataModel
import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.network.ApiService
import br.dev.singular.overview.data.network.response.GenreListResponse
import com.haroldadmin.cnradapter.NetworkResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

import java.io.IOException

class GenreRemoteDataSourceTest {

    private val api: ApiService = mockk()
    private val sut = GenreRemoteDataSource(api)

    @Test
    fun `getByMediaType should return genres list when API returns success`() = runTest {
        // arrange
        val genres = listOf(GenreDataModel(id = 1, name = "Action"))
        val response = mockk<GenreListResponse> {
            coEvery { this@mockk.genres } returns genres
        }
        coEvery { api.getGenres(any()) } returns NetworkResponse.Success(response, mockk(), 200)

        // act
        val result = sut.getByMediaType(MediaDataType.MOVIE)

        // assert
        assertEquals(genres, result)
    }

    @Test
    fun `getByMediaType should return empty list when API returns error`() = runTest {
        // arrange
        coEvery { api.getGenres(any()) } returns NetworkResponse.NetworkError(IOException())

        // act
        val result = sut.getByMediaType(MediaDataType.MOVIE)

        // assert
        assertEquals(emptyList<GenreDataModel>(), result)
    }

    @Test
    fun `getByMediaType should return empty list when exception occurs`() = runTest {
        // arrange
        coEvery { api.getGenres(any()) } throws Exception()

        // act
        val result = sut.getByMediaType(MediaDataType.MOVIE)

        // assert
        assertEquals(emptyList<GenreDataModel>(), result)
    }
}
