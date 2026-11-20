package br.dev.singular.overview.data.network.source

import br.dev.singular.overview.data.model.GenreDataModel
import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.network.TmdbApiService
import br.dev.singular.overview.data.network.response.ErrorResponse
import br.dev.singular.overview.data.network.response.GenreListResponse
import com.haroldadmin.cnradapter.NetworkResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

import java.io.IOException

class GenreRemoteDataSourceTest {

    private val api: TmdbApiService = mockk()
    private val sut = GenreRemoteDataSource(api)

    @Test
    fun `getByMediaType should return translated genres with their original names`() = runTest {
        // arrange
        coEvery { api.getGenres(any(), null) } returns success(
            GenreDataModel(id = 1, name = "Ação"),
            GenreDataModel(id = 2, name = "Comédia")
        )
        coEvery { api.getGenres(any(), "en-US") } returns success(
            GenreDataModel(id = 2, name = "Comedy"),
            GenreDataModel(id = 1, name = "Action")
        )

        // act
        val result = sut.getByMediaType(MediaDataType.MOVIE)

        // assert
        val expected = listOf(
            GenreDataModel(id = 1, name = "Ação", originalName = "Action"),
            GenreDataModel(id = 2, name = "Comédia", originalName = "Comedy")
        )
        assertEquals(expected, result)
        coVerify(exactly = 1) { api.getGenres(MediaDataType.MOVIE.key, null) }
        coVerify(exactly = 1) { api.getGenres(MediaDataType.MOVIE.key, "en-US") }
    }

    @Test
    fun `getByMediaType should leave original names empty when they can't be fetched`() = runTest {
        // arrange
        coEvery { api.getGenres(any(), null) } returns success(
            GenreDataModel(id = 1, name = "Ação")
        )
        coEvery { api.getGenres(any(), "en-US") } returns
                NetworkResponse.NetworkError(IOException())

        // act
        val result = sut.getByMediaType(MediaDataType.MOVIE)

        // assert
        assertEquals(listOf(GenreDataModel(id = 1, name = "Ação", originalName = "")), result)
    }

    @Test
    fun `getByMediaType should return empty list when API returns error`() = runTest {
        // arrange
        coEvery { api.getGenres(any(), any()) } returns NetworkResponse.NetworkError(IOException())

        // act
        val result = sut.getByMediaType(MediaDataType.MOVIE)

        // assert
        assertEquals(emptyList<GenreDataModel>(), result)
    }

    @Test
    fun `getByMediaType should return empty list when exception occurs`() = runTest {
        // arrange
        coEvery { api.getGenres(any(), any()) } throws Exception()

        // act
        val result = sut.getByMediaType(MediaDataType.MOVIE)

        // assert
        assertEquals(emptyList<GenreDataModel>(), result)
    }

    private fun success(
        vararg genres: GenreDataModel
    ): NetworkResponse<GenreListResponse, ErrorResponse> = NetworkResponse.Success(
        mockk<GenreListResponse> {
            coEvery { this@mockk.genres } returns genres.toList()
        },
        mockk()
    )
}
