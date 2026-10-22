package br.dev.singular.overview.data.network.source

import br.dev.singular.overview.data.model.PredictionInputDataModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

class PredictionRemoteDataSourceTest {

    private val sut: IPredictionRemoteDataSource = PredictionRemoteDataSource()

    @Test
    fun `getByMedia should return Success with a mocked affinity between 0 and 100`() = runTest {
        // arrange
        val media = PredictionInputDataModel(
            id = 1L,
            title = "Movie Title",
            genres = listOf("Comedy")
        )

        // act
        val result = sut.getByMedia(media)

        // assert
        assertTrue(result is DataResult.Success)
        val affinity = (result as DataResult.Success).data.affinityPercentage
        assertTrue(affinity in 0..100)
    }
}
