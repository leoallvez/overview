package br.dev.singular.overview.data.repository.prediction

import br.dev.singular.overview.data.model.PredictionDataModel
import br.dev.singular.overview.data.network.source.DataResult
import br.dev.singular.overview.data.network.source.IPredictionRemoteDataSource
import br.dev.singular.overview.data.util.mappers.domainToData.toData
import br.dev.singular.overview.domain.model.Prediction
import br.dev.singular.overview.domain.model.PredictionInput
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PredictionRemoteRepositoryTest {

    private val dataSource: IPredictionRemoteDataSource = mockk()

    private val media = PredictionInput(
        id = 1L,
        title = "Movie Title",
        genres = listOf("Comedy")
    )

    private lateinit var sut: PredictionRemoteRepository

    @Before
    fun setup() {
        sut = PredictionRemoteRepository(dataSource)
    }

    @Test
    fun `getByParam should request the prediction and return it available`() = runTest {
        // arrange
        val data = PredictionDataModel(affinityPercentage = 90)
        coEvery { dataSource.getByMedia(any()) } returns DataResult.Success(data)

        // act
        val result = sut.getByParam(media)

        // assert
        coVerify(exactly = 1) { dataSource.getByMedia(media.toData()) }
        assertEquals(Prediction.Available(affinityPercentage = 90), result)
    }

    @Test
    fun `getByParam should return error when data source has no affinity`() = runTest {
        // arrange
        coEvery { dataSource.getByMedia(any()) } returns DataResult.Success(PredictionDataModel())

        // act
        val result = sut.getByParam(media)

        // assert
        assertEquals(Prediction.Error, result)
    }

    @Test
    fun `getByParam should return error when data source returns error`() = runTest {
        // arrange
        coEvery { dataSource.getByMedia(any()) } returns DataResult.Error()

        // act
        val result = sut.getByParam(media)

        // assert
        assertEquals(Prediction.Error, result)
    }
}
