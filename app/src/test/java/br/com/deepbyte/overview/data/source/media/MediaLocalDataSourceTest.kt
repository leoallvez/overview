package br.com.deepbyte.overview.data.source.media

import br.com.deepbyte.overview.data.db.dao.MediaItemDao
import br.com.deepbyte.overview.data.model.MediaItem
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun update_updateMediaItem_daoUpdateMediaItemIsCalled() = runTest {
        // Arrange
        val mediaItem = MediaItem()
        // Act
        _source.update(mediaItem)
        // Assert
        coVerify { _dao.update(mediaItem) }
    }
}
