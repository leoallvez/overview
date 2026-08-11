package br.dev.singular.overview.data.local.source

import br.dev.singular.overview.data.BuildConfig
import br.dev.singular.overview.data.local.database.dao.MediaDao
import br.dev.singular.overview.data.model.MediaDataModel
import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.util.fakeMediaDataModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MediaLocalDataSourceTest {

    private val dao: MediaDao = mockk(relaxed = true)
    private val sut = MediaLocalDataSource(dao)

    @Test
    fun `insert should call dao insert with correct arguments`() = runTest {
        val models = listOf(fakeMediaDataModel)
        sut.insert(models)
        coVerify { dao.insert(fakeMediaDataModel) }
    }

    @Test
    fun `update should call dao update`() = runTest {
        val model = fakeMediaDataModel
        sut.update(model)
        coVerify { dao.update(model) }
    }

    @Test
    fun `delete should call dao delete with correct arguments`() = runTest {
        val models = listOf(fakeMediaDataModel)
        sut.delete(models)
        coVerify { dao.delete(fakeMediaDataModel) }
    }

    @Test
    fun `getAll should return list from dao`() = runTest {
        val expected = listOf(fakeMediaDataModel)
        coEvery { dao.getAll() } returns expected
        val result = sut.getAll()
        assertEquals(expected, result)
    }

    @Test
    fun `getById should return model from dao when found`() = runTest {
        val id = 1L
        coEvery { dao.getPage(id = id) } returns listOf(fakeMediaDataModel)
        val result = sut.getById(id)
        assertEquals(fakeMediaDataModel, result)
    }

    @Test
    fun `getPage should return MediaDataPage with items from dao`() = runTest {
        // arrange
        val page = 2
        val items = listOf(fakeMediaDataModel)
        val type = MediaDataType.MOVIE
        val pageSize = BuildConfig.PAGE_SIZE
        val offset = (page - 1) * pageSize

        coEvery {
            dao.getPage(
                type = type,
                limit = pageSize,
                isLiked = null,
                offset = offset
            )
        } returns items

        // act
        val result = sut.getPage(page = page, type = type)

        // assert
        assertEquals(items, result.items)
        assertEquals(page, result.page)
        assertTrue(result.isLastPage == (items.size < pageSize))
        coVerify {
            dao.getPage(
                type = type,
                limit = pageSize,
                isLiked = null,
                offset = offset
            )
        }
    }
}
