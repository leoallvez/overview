package br.dev.singular.overview.data.repository.media

import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.network.response.ListResponse
import br.dev.singular.overview.data.network.source.DataResult
import br.dev.singular.overview.data.network.source.ICatalogRemoteDataSource
import br.dev.singular.overview.data.network.source.IMediaRemoteDataSource
import br.dev.singular.overview.data.network.source.IVideoRemoteDataSource
import br.dev.singular.overview.data.util.createFakeCatalogDataModelList
import br.dev.singular.overview.data.util.createFakeVideoDataModelList
import br.dev.singular.overview.data.util.fakeTvShowDetailsDataModel
import br.dev.singular.overview.data.util.mappers.dataToDomain.toDomain
import br.dev.singular.overview.domain.model.TvShowDetails
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

class TvShowDetailsRemoteRepositoryTest {

    @MockK(relaxed = true)
    private lateinit var mediaDataSource: IMediaRemoteDataSource

    @MockK(relaxed = true)
    private lateinit var videoDataSource: IVideoRemoteDataSource

    @MockK(relaxed = true)
    private lateinit var catalogDataSource: ICatalogRemoteDataSource

    private lateinit var sut: GetById<TvShowDetails>

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        sut = TvShowDetailsRemoteRepository(
            mediaDataSource,
            videoDataSource,
            catalogDataSource,
        )
    }

    @Test
    fun `should return TvShowDetails domain object when data source returns success`() = runTest {
        // Arrange
        val id = 1L
        coEvery {
            mediaDataSource.getTvShowById(id)
        } returns DataResult.Success(fakeTvShowDetailsDataModel)

        // Act
        val result = sut.getById(id)

        // Assert
        result shouldBeEqualTo fakeTvShowDetailsDataModel.toDomain()
        coVerify(exactly = 1) { mediaDataSource.getTvShowById(id) }
    }

    @Test
    fun `should return null when data source returns an error`() = runTest {
        // Arrange
        val id = 1L
        coEvery { mediaDataSource.getTvShowById(id) } returns DataResult.Error()

        // Act
        val result = sut.getById(id)

        // Assert
        result.shouldBeNull()
        coVerify(exactly = 1) { mediaDataSource.getTvShowById(id) }
        coVerify(exactly = 0) { videoDataSource.getVideos(any(), any()) }
        coVerify(exactly = 0) { catalogDataSource.getCatalogsByMedia(any(), any()) }
    }

    @Test
    fun `should include videos in TvShowDetails when video data source returns success`() = runTest {
        // Arrange
        val id = 1L
        coEvery {
            mediaDataSource.getTvShowById(id)
        } returns DataResult.Success(fakeTvShowDetailsDataModel)

        coEvery {
            videoDataSource.getVideos(id, MediaDataType.TV)
        } returns DataResult.Success(
            data = ListResponse(results = createFakeVideoDataModelList(count = 3))
        )

        // Act
        val result = sut.getById(id)

        // Assert
        result.shouldNotBeNull()
        result.videos shouldHaveSize 3
        coVerify(exactly = 1) { videoDataSource.getVideos(id, MediaDataType.TV) }
    }

    @Test
    fun `should include catalogs in TvShowDetails when catalog data source returns a list`() = runTest {
        // Arrange
        val id = 1L
        coEvery {
            mediaDataSource.getTvShowById(id)
        } returns DataResult.Success(fakeTvShowDetailsDataModel)

        coEvery {
            catalogDataSource.getCatalogsByMedia(id, MediaDataType.TV)
        } returns createFakeCatalogDataModelList(count = 3)

        // Act
        val result = sut.getById(id)

        // Assert
        result.shouldNotBeNull()
        result.catalogs shouldHaveSize 3
        coVerify(exactly = 1) { catalogDataSource.getCatalogsByMedia(id, MediaDataType.TV) }
    }

    @Test
    fun `should return TvShowDetails even when video data source returns an error`() = runTest {
        // Arrange
        val id = 1L
        coEvery {
            mediaDataSource.getTvShowById(id)
        } returns DataResult.Success(fakeTvShowDetailsDataModel)

        coEvery {
            videoDataSource.getVideos(id, MediaDataType.TV)
        } returns DataResult.Error()

        // Act
        val result = sut.getById(id)

        // Assert
        result.shouldNotBeNull()
        result.videos.shouldBeEmpty()
        coVerify(exactly = 1) { videoDataSource.getVideos(id, MediaDataType.TV) }
    }
}
