package br.com.deepbyte.overview.data.repository.genre

import br.com.deepbyte.overview.data.model.media.Genre
import br.com.deepbyte.overview.data.model.media.MediaType
import br.com.deepbyte.overview.data.model.media.MediaTypeWithGenres
import br.com.deepbyte.overview.data.source.genre.GenreLocalDataSource
import br.com.deepbyte.overview.data.source.genre.IGenreRemoteDataSource
import br.com.deepbyte.overview.data.source.media.MediaTypeEnum.ALL
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldNotBeEmpty
import org.junit.Before
import org.junit.Test

class GenreRepositoryTest {

    @MockK(relaxed = true)
    private lateinit var _remoteSource: IGenreRemoteDataSource

    @MockK(relaxed = true)
    private lateinit var _localSource: GenreLocalDataSource

    private lateinit var _repository: GenreRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        _repository = GenreRepository(_localSource,_remoteSource)
    }

    @Test
    fun `should be an empty list when local return empty`() = runTest {
        // Arrange
        coEvery {_localSource.getGenresWithMediaType(any()) } returns listOf()
        // Act
        val result = _repository.getItemsByMediaType(ALL)
        // Assert
        result.shouldBeEmpty()
    }

    @Test
    fun `should not be an empty list when local return a not empty list`() = runTest {
        // Arrange
        val content = MediaTypeWithGenres(
            mediaType = MediaType(key = ALL.key),
            genres = listOf(Genre())
        )
        coEvery {_localSource.getGenresWithMediaType(any()) } returns listOf(content)
        // Act
        val result = _repository.getItemsByMediaType(ALL)
        // Assert
        result.shouldNotBeEmpty()
    }

    @Test
    fun `should be call getItemByMediaType when cacheWithType was called`() = runTest {
        // Act
        _repository.cacheWithType(ALL)
        // Assert
        coVerify { _remoteSource.getItemByMediaType(any()) }
    }
}
