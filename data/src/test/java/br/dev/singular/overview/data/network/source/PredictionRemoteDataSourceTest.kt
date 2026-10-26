package br.dev.singular.overview.data.network.source

import br.dev.singular.overview.data.BuildConfig
import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.model.PredictionFavoriteDataModel
import br.dev.singular.overview.data.model.PredictionInputDataModel
import br.dev.singular.overview.data.network.JevApiService
import br.dev.singular.overview.data.network.request.JevMedia
import br.dev.singular.overview.data.network.request.JevRequest
import br.dev.singular.overview.data.network.request.JevTitle
import br.dev.singular.overview.data.network.response.ErrorResponse
import br.dev.singular.overview.data.network.response.JevAnswer
import br.dev.singular.overview.data.network.response.JevResponse
import com.haroldadmin.cnradapter.NetworkResponse
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class PredictionRemoteDataSourceTest {

    private val api: JevApiService = mockk()
    private val sut: IPredictionRemoteDataSource = PredictionRemoteDataSource(api)

    private val input = PredictionInputDataModel(
        id = 603L,
        type = MediaDataType.MOVIE,
        title = "The Matrix",
        genres = listOf("Science Fiction", "Action"),
        favorites = listOf(
            PredictionFavoriteDataModel(title = "Breaking Bad", type = MediaDataType.TV)
        ),
        favoriteGenres = mapOf("Drama" to 1, "Crime" to 1)
    )

    @Test
    fun `getByMedia should send the media and the favorites to jev`() = runTest {
        // arrange
        val request = slot<JevRequest>()
        coEvery { api.decide(capture(request)) } returns success(score = 2.0)

        // act
        sut.getByMedia(input)

        // assert
        with(request.captured) {
            assertEquals(BuildConfig.JEV_MODEL, model)
            assertEquals(
                JevMedia(
                    originalTitle = "The Matrix",
                    type = MediaDataType.MOVIE,
                    genres = listOf("Science Fiction", "Action")
                ),
                state.media
            )
            assertEquals(
                listOf(JevTitle(originalTitle = "Breaking Bad", type = MediaDataType.TV)),
                state.favorites.titles
            )
            assertEquals(mapOf("Drama" to 1, "Crime" to 1), state.favorites.genres)
            val question = questions.getValue("affinity")
            assertEquals("score", question.type)
            assertEquals(5, question.criteria.size)
        }
    }

    @Test
    fun `getByMedia should convert the score to a percentage`() = runTest {
        // arrange
        coEvery { api.decide(any()) } returns success(score = 3.36)

        // act
        val result = sut.getByMedia(input)

        // assert
        assertTrue(result is DataResult.Success)
        assertEquals(84, (result as DataResult.Success).data.affinityPercentage)
    }

    @Test
    fun `getByMedia should return a null percentage when the answer has no score`() = runTest {
        // arrange
        coEvery { api.decide(any()) } returns NetworkResponse.Success(
            JevResponse(),
            mockk()
        )

        // act
        val result = sut.getByMedia(input)

        // assert
        assertTrue(result is DataResult.Success)
        assertNull((result as DataResult.Success).data.affinityPercentage)
    }

    @Test
    fun `getByMedia should return Error when the request fails`() = runTest {
        // arrange
        coEvery { api.decide(any()) } returns NetworkResponse.NetworkError(IOException())

        // act
        val result = sut.getByMedia(input)

        // assert
        assertTrue(result is DataResult.Error)
    }

    private fun success(score: Double): NetworkResponse<JevResponse, ErrorResponse> {
        val body = JevResponse(
            answers = mapOf("affinity" to JevAnswer(score = score, confidence = 0.6))
        )
        return NetworkResponse.Success(body, mockk())
    }
}
