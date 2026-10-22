package br.dev.singular.overview.data.local.source

import br.dev.singular.overview.data.local.database.dao.PendingFavoriteDao
import br.dev.singular.overview.data.model.MediaDataModel
import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.model.PendingFavoriteDataModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Date

class PendingFavoriteLocalDataSourceTest {

    private val dao: PendingFavoriteDao = mockk(relaxed = true)
    private val sut = PendingFavoriteLocalDataSource(dao)

    @Test
    fun `getAll should map the pending favorites to liked media models`() = runTest {
        // arrange
        val date = Date(1000)
        coEvery { dao.getAll() } returns listOf(
            PendingFavoriteDataModel(
                id = 7,
                type = MediaDataType.TV,
                title = "Dark",
                posterPath = "/dark.jpg",
                lastUpdate = date
            )
        )

        // act
        val result = sut.getAll()

        // assert
        val expected = MediaDataModel(
            id = 7,
            name = "Dark",
            title = "Dark",
            posterPath = "/dark.jpg",
            type = MediaDataType.TV,
            isLiked = true,
            lastUpdate = date
        )
        assertEquals(listOf(expected), result)
    }

    @Test
    fun `clear should delete all pending favorites`() = runTest {
        // act
        sut.clear()

        // assert
        coVerify(exactly = 1) { dao.deleteAll() }
    }
}
