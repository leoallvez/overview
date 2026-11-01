package br.dev.singular.overview.data.repository.media

import br.dev.singular.overview.data.local.source.IGenreLocalDataSource
import br.dev.singular.overview.data.model.GenreDataModel
import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.network.response.ListResponse
import br.dev.singular.overview.data.network.source.DataResult
import br.dev.singular.overview.data.network.source.ICatalogRemoteDataSource
import br.dev.singular.overview.data.network.source.IVideoRemoteDataSource
import br.dev.singular.overview.data.util.createFakeCatalogDataModelList
import br.dev.singular.overview.data.util.createFakeVideoDataModelList
import br.dev.singular.overview.data.util.mappers.dataToDomain.toDomain
import br.dev.singular.overview.domain.model.Genre
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import kotlin.coroutines.cancellation.CancellationException

class MediaDetailsExtensionsTest {

    private val dataSource: IGenreLocalDataSource = mockk()
    private val videoDataSource: IVideoRemoteDataSource = mockk()
    private val catalogDataSource: ICatalogRemoteDataSource = mockk()

    @Test
    fun `fetchVideos should map the videos when the data source returns success`() = runTest {
        // arrange
        val videos = createFakeVideoDataModelList(count = 2)
        coEvery {
            videoDataSource.getVideos(1L, MediaDataType.TV)
        } returns DataResult.Success(ListResponse(results = videos))

        // act
        val result = videoDataSource.fetchVideos(1L, MediaDataType.TV)

        // assert
        result shouldBeEqualTo videos.map { it.toDomain() }
    }

    @Test
    fun `fetchVideos should return an empty list when the data source returns an error`() =
        runTest {
            // arrange
            coEvery { videoDataSource.getVideos(any(), any()) } returns DataResult.Error()

            // act
            val result = videoDataSource.fetchVideos(1L, MediaDataType.MOVIE)

            // assert
            result.shouldBeEmpty()
        }

    @Test
    fun `fetchCatalogs should map the catalogs returned by the data source`() = runTest {
        // arrange
        val catalogs = createFakeCatalogDataModelList(count = 2)
        coEvery { catalogDataSource.getCatalogsByMedia(1L, MediaDataType.MOVIE) } returns catalogs

        // act
        val result = catalogDataSource.fetchCatalogs(1L, MediaDataType.MOVIE)

        // assert
        result shouldBeEqualTo catalogs.map { it.toDomain() }
    }

    @Test
    fun `fillOriginalNames should fill the original names found in the local cache`() = runTest {
        // arrange
        val genres = listOf(Genre(id = 1L, name = "Ação"), Genre(id = 2L, name = "Comédia"))
        coEvery {
            dataSource.getByIds(listOf(1L, 2L))
        } returns listOf(GenreDataModel(id = 1L, name = "Ação", originalName = "Action"))

        // act
        val result = dataSource.fillOriginalNames(genres)

        // assert
        result shouldBeEqualTo listOf(
            Genre(id = 1L, name = "Ação", originalName = "Action"),
            Genre(id = 2L, name = "Comédia", originalName = "")
        )
    }

    @Test
    fun `fillOriginalNames should leave the original names empty when the local cache fails`() =
        runTest {
            // arrange
            val genres = listOf(Genre(id = 1L, name = "Ação"))
            coEvery { dataSource.getByIds(any()) } throws Exception()

            // act
            val result = dataSource.fillOriginalNames(genres)

            // assert
            result shouldBeEqualTo listOf(Genre(id = 1L, name = "Ação", originalName = ""))
        }

    @Test(expected = CancellationException::class)
    fun `fillOriginalNames should rethrow the cancellation`() = runTest {
        // arrange
        coEvery { dataSource.getByIds(any()) } throws CancellationException()

        // act
        dataSource.fillOriginalNames(listOf(Genre(id = 1L, name = "Ação")))
    }
}
