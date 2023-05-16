package br.com.deepbyte.overview.data.source.media

import br.com.deepbyte.overview.data.db.dao.MediaItemDao
import br.com.deepbyte.overview.data.model.MediaItem
import br.com.deepbyte.overview.data.source.media.local.MediaLocalDataSource
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class MediaLocalDataSourceTest {

    @MockK(relaxed = true)
    private lateinit var _dao: MediaItemDao
    private lateinit var _source: MediaLocalDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        _source = MediaLocalDataSource(_dao)
    }

    @Test
    fun `should call dao update when call update`() = runTest {
        // Arrange
        val media = MediaItem()
        // Act
        _source.update(media)
        // Assert
        coVerify { _dao.update(media) }
    }
}
