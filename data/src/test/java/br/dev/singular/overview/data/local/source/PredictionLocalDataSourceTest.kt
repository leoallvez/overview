package br.dev.singular.overview.data.local.source

import br.dev.singular.overview.data.local.database.dao.PredictionDao
import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.model.PredictionDataModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PredictionLocalDataSourceTest {

    private val dao: PredictionDao = mockk(relaxed = true)
    private val sut = PredictionLocalDataSource(dao)

    private val model = PredictionDataModel(
        id = 1L,
        type = MediaDataType.TV,
        affinityPercentage = 80
    )

    @Test
    fun `insert should save the prediction`() = runTest {
        // act
        sut.insert(model)

        // assert
        coVerify(exactly = 1) { dao.insert(model) }
    }

    @Test
    fun `get should return the prediction of the media`() = runTest {
        // arrange
        coEvery { dao.get(1L, MediaDataType.TV) } returns model

        // act
        val result = sut.get(1L, MediaDataType.TV)

        // assert
        assertEquals(model, result)
    }

    @Test
    fun `get should return null when the media has no prediction`() = runTest {
        // arrange
        coEvery { dao.get(any(), any()) } returns null

        // act
        val result = sut.get(1L, MediaDataType.MOVIE)

        // assert
        assertNull(result)
    }

    @Test
    fun `clear should delete all predictions`() = runTest {
        // act
        sut.clear()

        // assert
        coVerify(exactly = 1) { dao.deleteAll() }
    }
}
