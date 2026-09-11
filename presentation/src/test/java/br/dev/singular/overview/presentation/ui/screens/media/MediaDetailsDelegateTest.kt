package br.dev.singular.overview.presentation.ui.screens.media

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.QueryState
import br.dev.singular.overview.domain.usecase.ICatalogQueryStateUseCase
import br.dev.singular.overview.domain.usecase.media.IMediaPersistenceUseCase
import br.dev.singular.overview.presentation.createCatalogUiModelMock
import br.dev.singular.overview.presentation.createMediaMock
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test

class MediaDetailsDelegateTest {

    @MockK
    private lateinit var mediaUseCase: IMediaPersistenceUseCase

    @MockK
    private lateinit var queryUseCase: ICatalogQueryStateUseCase

    private lateinit var sut: IMediaDetailsDelegate

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        sut = MediaDetailsDelegate(mediaUseCase, queryUseCase)
    }

    @Test
    fun `getIsLiked should return true when media is liked in persistence`() = runTest {
        // arrange
        val id = 1L
        val media = createMediaMock().copy(id = id, isLiked = true)
        coEvery { mediaUseCase.getById(id) } returns media

        // act
        val result = sut.getIsLiked(id)

        // assert
        result shouldBeEqualTo true
        coVerify(exactly = 1) { mediaUseCase.getById(id) }
    }

    @Test
    fun `getIsLiked should return false when media is not found`() = runTest {
        // arrange
        coEvery { mediaUseCase.getById(any()) } returns null

        // act
        val result = sut.getIsLiked(1L)

        // assert
        result shouldBeEqualTo false
    }

    @Test
    fun `toggleLike should return false and save as false when current is true`() = runTest {
        // arrange
        val media = createMediaMock().copy(id = 1L, isLiked = true)
        val mediaSlot = slot<Media>()
        coEvery { mediaUseCase.save(capture(mediaSlot)) } returns Unit

        // act
        val result = sut.toggleLike(media)

        // assert
        result shouldBeEqualTo false
        mediaSlot.captured.id shouldBeEqualTo 1L
        mediaSlot.captured.isLiked shouldBeEqualTo false
        coVerify(exactly = 1) { mediaUseCase.save(any()) }
    }

    @Test
    fun `toggleLike should return true and save as true when current is false`() = runTest {
        // arrange
        val media = createMediaMock().copy(id = 1L, isLiked = false)
        val mediaSlot = slot<Media>()
        coEvery { mediaUseCase.save(capture(mediaSlot)) } returns Unit

        // act
        val result = sut.toggleLike(media)

        // assert
        result shouldBeEqualTo true
        mediaSlot.captured.id shouldBeEqualTo 1L
        mediaSlot.captured.isLiked shouldBeEqualTo true
        coVerify(exactly = 1) { mediaUseCase.save(any()) }
    }

    @Test
    fun `selectCatalog should update current query state with new catalog`() = runTest {
        // arrange
        val catalogUi = createCatalogUiModelMock().copy(id = 10L)
        val currentQuery = QueryState(type = MediaType.MOVIE)
        val querySlot = slot<QueryState>()
        coEvery { queryUseCase.get() } returns currentQuery
        coEvery { queryUseCase.save(capture(querySlot)) } returns Unit

        // act
        sut.selectCatalog(catalogUi)

        // assert
        querySlot.captured.catalog?.id shouldBeEqualTo 10L
        querySlot.captured.type shouldBeEqualTo MediaType.MOVIE
        coVerify(exactly = 1) { queryUseCase.save(any()) }
    }

    @Test
    fun `selectCatalog should use default query state when current is null`() = runTest {
        // arrange
        val catalogUi = createCatalogUiModelMock().copy(id = 20L)
        val querySlot = slot<QueryState>()
        coEvery { queryUseCase.get() } returns null
        coEvery { queryUseCase.save(capture(querySlot)) } returns Unit

        // act
        sut.selectCatalog(catalogUi)

        // assert
        querySlot.captured.catalog?.id shouldBeEqualTo 20L
        coVerify(exactly = 1) { queryUseCase.save(any()) }
    }
}
