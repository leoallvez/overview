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
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

class MediaDetailsExtrasRepositoryTest {

    private val videoDataSource: IVideoRemoteDataSource = mockk()
    private val catalogDataSource: ICatalogRemoteDataSource = mockk()
    private val genreDataSource: IGenreLocalDataSource = mockk()

    private val sut = MediaDetailsExtrasRepository(
        videoDataSource,
        catalogDataSource,
        genreDataSource,
    )

    @Test
    fun `fetch should return the videos, catalogs and genres of the media`() = runTest {
        // arrange
        val videos = createFakeVideoDataModelList(count = 2)
        val catalogs = createFakeCatalogDataModelList(count = 3)
        coEvery {
            videoDataSource.getVideos(1L, MediaDataType.MOVIE)
        } returns DataResult.Success(ListResponse(results = videos))
        coEvery { catalogDataSource.getCatalogsByMedia(1L, MediaDataType.MOVIE) } returns catalogs
        coEvery {
            genreDataSource.getByIds(listOf(1L))
        } returns listOf(GenreDataModel(id = 1L, name = "Ação", originalName = "Action"))

        // act
        val result = sut.fetch(1L, MediaDataType.MOVIE, listOf(Genre(id = 1L, name = "Ação")))

        // assert
        result shouldBeEqualTo MediaDetailsExtras(
            videos = videos.map { it.toDomain() },
            catalogs = catalogs.map { it.toDomain() },
            genres = listOf(Genre(id = 1L, name = "Ação", originalName = "Action")),
        )
        coVerify(exactly = 1) { videoDataSource.getVideos(1L, MediaDataType.MOVIE) }
        coVerify(exactly = 1) { catalogDataSource.getCatalogsByMedia(1L, MediaDataType.MOVIE) }
    }

    @Test
    fun `fetch should return empty extras when the sources have nothing`() = runTest {
        // arrange
        coEvery { videoDataSource.getVideos(any(), any()) } returns DataResult.Error()
        coEvery { catalogDataSource.getCatalogsByMedia(any(), any()) } returns emptyList()
        coEvery { genreDataSource.getByIds(any()) } returns emptyList()

        // act
        val result = sut.fetch(2L, MediaDataType.TV, emptyList())

        // assert
        result.videos.shouldBeEmpty()
        result.catalogs.shouldBeEmpty()
        result.genres.shouldBeEmpty()
    }
}
