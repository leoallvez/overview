package br.dev.singular.overview.data.repository.media

import br.dev.singular.overview.data.local.source.IGenreLocalDataSource
import br.dev.singular.overview.data.model.GenreDataModel
import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.network.response.ListResponse
import br.dev.singular.overview.data.network.source.DataResult
import br.dev.singular.overview.data.network.source.ICatalogRemoteDataSource
import br.dev.singular.overview.data.network.source.IMediaRemoteDataSource
import br.dev.singular.overview.data.network.source.IVideoRemoteDataSource
import br.dev.singular.overview.data.util.createFakeCatalogDataModelList
import br.dev.singular.overview.data.util.createFakeVideoDataModelList
import br.dev.singular.overview.data.util.fakeMovieDetailsDataModel
import br.dev.singular.overview.data.util.mappers.dataToDomain.toDomain
import br.dev.singular.overview.domain.model.Genre
import br.dev.singular.overview.domain.model.MovieDetails
import br.dev.singular.overview.domain.repository.GetById
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldHaveSize
import org.amshove.kluent.shouldNotBeNull
import org.junit.Before
import org.junit.Test

class MovieDetailsRemoteRepositoryTest {

    @MockK(relaxed = true)
    private lateinit var mediaDataSource: IMediaRemoteDataSource

    @MockK(relaxed = true)
    private lateinit var videoDataSource: IVideoRemoteDataSource

    @MockK(relaxed = true)
    private lateinit var catalogDataSource: ICatalogRemoteDataSource

    @MockK(relaxed = true)
    private lateinit var genreDataSource: IGenreLocalDataSource

    private lateinit var sut: GetById<MovieDetails>

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        sut = MovieDetailsRemoteRepository(
            mediaDataSource,
            MediaDetailsExtrasRepository(videoDataSource, catalogDataSource, genreDataSource),
        )
    }

    @Test
    fun `should return MovieDetails domain object when data source returns success`() = runTest {
        // Arrange
        val id = 1L
        coEvery {
            mediaDataSource.getMovieById(id)
        } returns DataResult.Success(fakeMovieDetailsDataModel)

        // Act
        val result = sut.getById(id)

        // Assert
        result shouldBeEqualTo fakeMovieDetailsDataModel.toDomain()
        coVerify(exactly = 1) { mediaDataSource.getMovieById(id) }
    }

    @Test
    fun `should return null when data source returns an error`() = runTest {
        // Arrange
        val id = 1L
        coEvery { mediaDataSource.getMovieById(id) } returns DataResult.Error()

        // Act
        val result = sut.getById(id)

        // Assert
        result.shouldBeNull()
        coVerify(exactly = 1) { mediaDataSource.getMovieById(id) }
        coVerify(exactly = 0) { videoDataSource.getVideos(any(), any()) }
        coVerify(exactly = 0) { catalogDataSource.getCatalogsByMedia(any(), any()) }
    }

    @Test
    fun `should include videos in MovieDetails when video data source returns success`() = runTest {
        // Arrange
        val id = 1L
        coEvery {
            mediaDataSource.getMovieById(id)
        } returns DataResult.Success(fakeMovieDetailsDataModel)

        coEvery {
            videoDataSource.getVideos(id, MediaDataType.MOVIE)
        } returns DataResult.Success(
            data = ListResponse(results = createFakeVideoDataModelList(count = 3))
        )

        // Act
        val result = sut.getById(id)

        // Assert
        result.shouldNotBeNull()
        result.videos shouldHaveSize 3
        coVerify(exactly = 1) { videoDataSource.getVideos(id, MediaDataType.MOVIE) }
    }

    @Test
    fun `should include catalogs in MovieDetails when catalog data source returns a list`() =
        runTest {
            // Arrange
            val id = 1L
            coEvery {
                mediaDataSource.getMovieById(id)
            } returns DataResult.Success(fakeMovieDetailsDataModel)

            coEvery {
                catalogDataSource.getCatalogsByMedia(id, MediaDataType.MOVIE)
            } returns createFakeCatalogDataModelList(count = 3)

            // Act
            val result = sut.getById(id)

            // Assert
            result.shouldNotBeNull()
            result.catalogs shouldHaveSize 3
            coVerify(exactly = 1) { catalogDataSource.getCatalogsByMedia(id, MediaDataType.MOVIE) }
        }

    @Test
    fun `should return MovieDetails even when video data source returns an error`() = runTest {
        // Arrange
        val id = 1L
        coEvery {
            mediaDataSource.getMovieById(id)
        } returns DataResult.Success(fakeMovieDetailsDataModel)

        coEvery {
            videoDataSource.getVideos(id, MediaDataType.MOVIE)
        } returns DataResult.Error()

        // Act
        val result = sut.getById(id)

        // Assert
        result.shouldNotBeNull()
        result.videos.shouldBeEmpty()
        coVerify(exactly = 1) { videoDataSource.getVideos(id, MediaDataType.MOVIE) }
    }

    @Test
    fun `should fill the genres original names from the local cache`() = runTest {
        // Arrange
        val id = 1L
        val genres = listOf(
            GenreDataModel(id = 1L, name = "Ação"),
            GenreDataModel(id = 2L, name = "Comédia")
        )
        coEvery {
            mediaDataSource.getMovieById(id)
        } returns DataResult.Success(fakeMovieDetailsDataModel.copy(genres = genres))

        coEvery {
            genreDataSource.getByIds(listOf(1L, 2L))
        } returns listOf(GenreDataModel(id = 1L, name = "Ação", originalName = "Action"))

        // Act
        val result = sut.getById(id)

        // Assert
        result.shouldNotBeNull()
        result.genres shouldBeEqualTo listOf(
            Genre(id = 1L, name = "Ação", originalName = "Action"),
            Genre(id = 2L, name = "Comédia", originalName = "")
        )
        coVerify(exactly = 1) { genreDataSource.getByIds(listOf(1L, 2L)) }
    }

    @Test
    fun `should keep the genres without original names when the local cache fails`() = runTest {
        // Arrange
        val id = 1L
        val genres = listOf(GenreDataModel(id = 1L, name = "Ação"))
        coEvery {
            mediaDataSource.getMovieById(id)
        } returns DataResult.Success(fakeMovieDetailsDataModel.copy(genres = genres))

        coEvery { genreDataSource.getByIds(any()) } throws Exception()

        // Act
        val result = sut.getById(id)

        // Assert
        result.shouldNotBeNull()
        result.genres shouldBeEqualTo listOf(Genre(id = 1L, name = "Ação", originalName = ""))
    }
}
