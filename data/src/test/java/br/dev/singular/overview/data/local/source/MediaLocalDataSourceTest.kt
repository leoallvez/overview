package br.dev.singular.overview.data.local.source

import br.dev.singular.overview.data.BuildConfig
import br.dev.singular.overview.data.local.database.dao.MediaDao
import br.dev.singular.overview.data.model.MediaDataModel
import br.dev.singular.overview.data.model.MediaDataType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class MediaLocalDataSourceTest {

    private val dao: MediaDao = mockk(relaxed = true)
    private val sut = MediaLocalDataSource(dao)

    @Test
    fun `insert should call dao insert`() = runTest {
        val models = listOf(MediaDataModel(id = 1))
        sut.insert(models)
        coVerify { dao.insert(any()) }
    }

    @Test
    fun `delete should call dao delete`() = runTest {
        val models = listOf(MediaDataModel(id = 1))
        sut.delete(models)
        coVerify { dao.delete(any()) }
    }

    @Test
    fun `getAll should return list from dao`() = runTest {
        val expected = listOf(MediaDataModel(id = 1))
        coEvery { dao.getAll() } returns expected
        val result = sut.getAll()
        assertEquals(expected, result)
    }

    @Test
    fun `getPage should return MediaDataPage with items from dao`() = runTest {
        // arrange
        val page = 1
        val items = listOf(MediaDataModel(id = 1))
        coEvery { 
            dao.getPage(
                type = any(),
                limit = any(),
                isLiked = any(),
                offset = any()
            ) 
        } returns items

        // act
        val result = sut.getPage(page = page, type = MediaDataType.MOVIE)

        // assert
        assertEquals(items, result.items)
        assertEquals(page, result.page)
        // isLastPage depends on items.size < BuildConfig.PAGE_SIZE
        // We don't strictly know PAGE_SIZE here if it's from BuildConfig, but we can assume it's > 1 for this test
    }
}
