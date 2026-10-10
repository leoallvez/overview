package br.dev.singular.overview.data.repository.prediction

import br.dev.singular.overview.data.local.source.IPredictionLocalDataSource
import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.model.PredictionDataModel
import br.dev.singular.overview.data.network.source.DataResult
import br.dev.singular.overview.data.network.source.IPredictionRemoteDataSource
import br.dev.singular.overview.data.util.mappers.domainToData.toData
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.Prediction
import br.dev.singular.overview.domain.model.PredictionInput
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PredictionRepositoryTest {

    private val localSource: IPredictionLocalDataSource = mockk(relaxed = true)

    private val remoteSource: IPredictionRemoteDataSource = mockk()

    private val media = PredictionInput(
        id = 1L,
        type = MediaType.MOVIE,
        title = "Movie Title",
        genres = listOf("Comedy")
    )

    private lateinit var sut: PredictionRepository

    @Before
    fun setup() {
        coEvery { localSource.get(any(), any()) } returns null
        sut = PredictionRepository(localSource, remoteSource)
    }

    @Test
    fun `getByParam should return the cached prediction without requesting the remote`() =
        runTest {
            // arrange
            val cached = PredictionDataModel(
                id = 1L,
                type = MediaDataType.MOVIE,
                affinityPercentage = 75
            )
            coEvery { localSource.get(1L, MediaDataType.MOVIE) } returns cached

            // act
            val result = sut.getByParam(media)

            // assert
            assertEquals(Prediction.Available(affinityPercentage = 75), result)
            coVerify(exactly = 0) { remoteSource.getByMedia(any()) }
        }

    @Test
    fun `getByParam should request the remote and cache the prediction when available`() =
        runTest {
            // arrange
            val data = PredictionDataModel(affinityPercentage = 90)
            coEvery { remoteSource.getByMedia(any()) } returns DataResult.Success(data)

            // act
            val result = sut.getByParam(media)

            // assert
            assertEquals(Prediction.Available(affinityPercentage = 90), result)
            coVerify(exactly = 1) { remoteSource.getByMedia(media.toData()) }
            coVerify(exactly = 1) {
                localSource.insert(
                    PredictionDataModel(
                        id = 1L,
                        type = MediaDataType.MOVIE,
                        affinityPercentage = 90
                    )
                )
            }
        }

    @Test
    fun `getByParam should return error and cache nothing when remote has no affinity`() =
        runTest {
            // arrange
            coEvery {
                remoteSource.getByMedia(any())
            } returns DataResult.Success(PredictionDataModel())

            // act
            val result = sut.getByParam(media)

            // assert
            assertEquals(Prediction.Error, result)
            coVerify(exactly = 0) { localSource.insert(any()) }
        }

    @Test
    fun `getByParam should return error and cache nothing when remote returns error`() =
        runTest {
            // arrange
            coEvery { remoteSource.getByMedia(any()) } returns DataResult.Error()

            // act
            val result = sut.getByParam(media)

            // assert
            assertEquals(Prediction.Error, result)
            coVerify(exactly = 0) { localSource.insert(any()) }
        }
}
