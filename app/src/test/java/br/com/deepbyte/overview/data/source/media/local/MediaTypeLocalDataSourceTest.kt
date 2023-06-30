package br.com.deepbyte.overview.data.source.media.local

import br.com.deepbyte.overview.data.db.dao.MediaTypeDao
import br.com.deepbyte.overview.data.model.media.MediaType
import br.com.deepbyte.overview.data.source.media.MediaTypeEnum.ALL
import br.com.deepbyte.overview.data.source.media.MediaTypeEnum.MOVIE
import br.com.deepbyte.overview.data.source.media.MediaTypeEnum.TV_SHOW
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldNotBeEmpty
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class MediaTypeLocalDataSourceTest {

    @MockK(relaxed = true)
    private lateinit var _dao: MediaTypeDao

    private lateinit var _source: MediaTypeLocalDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        _source = MediaTypeLocalDataSource(_dao)
    }

    @Test
    fun `should call dao getAll when call source getAll`() = runTest {
        // Arrange
        // Act
        _source.getAll()
        // Assert
        coVerify { _dao.getAll() }
    }

    @Test
    fun `should call dao isEmpty when call source isEmpty`() = runTest {
        // Arrange
        // Act
        val isEmpty: Boolean = _source.isEmpty()
        // Assert
        coVerify { _dao.getAll() }
        Assert.assertTrue(isEmpty)
    }

    @Test
    fun `should source return an empty list when dao return an empty list`() {
        // Arrange
        every { _dao.getAll() } returns emptyList()
        // Act
        val list = _source.getAll()
        // Assert
        list.shouldBeEmpty()
    }

    @Test
    fun `should source return an not empty list when dao return an not empty list`() {
        // Arrange
        every { _dao.getAll() } returns getMediaTypeList()
        // Act
        val list = _source.getAll()
        // Assert
        list.shouldNotBeEmpty()
    }

    @Test
    fun `should be source isEmpty true when dao return an empty list`() {
        // Arrange
        every { _dao.getAll() } returns emptyList()
        // Act
        val isEmpty = _source.isEmpty()
        // Assert
        isEmpty.shouldBeTrue()
    }

    @Test
    fun `should be source isEmpty false when dao return an not empty list`() {
        // Arrange
        every { _dao.getAll() } returns getMediaTypeList()
        // Act
        val isEmpty = _source.isEmpty()
        // Assert
        isEmpty.shouldBeFalse()
    }

    private fun getMediaTypeList() =
        listOf(MediaType(key = MOVIE.key), MediaType(key = TV_SHOW.key))

    @Test
    fun `should call dao insert when call source insert`() = runTest {
        // Arrange
        val list = listOf(MediaType(key = ALL.key))
        // Act
        _source.insert(list)
        // Assert
        coVerify { _dao.insert(any()) }
    }
}
