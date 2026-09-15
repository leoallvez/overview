package br.dev.singular.overview.presentation.ui.screens.media

import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.QueryState
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.ICatalogQueryStateUseCase
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.media.IMediaPersistenceUseCase
import br.dev.singular.overview.domain.usecase.media.IToggleFavoriteUseCase
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

    @MockK
    private lateinit var favoriteUseCase: IToggleFavoriteUseCase

    private lateinit var sut: IMediaDetailsDelegate

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        sut = MediaDetailsDelegate(mediaUseCase, queryUseCase, favoriteUseCase)
    }

    @Test
    fun `getIsLiked should return true when media is liked in persistence`() = runTest {
        // arrange
        val id = 1L
        val media = createMediaMock().copy(id = id, isLiked = true)
        coEvery { mediaUseCase.getById(id, MediaType.MOVIE) } returns media

        // act
        val result = sut.getIsLiked(id, MediaType.MOVIE)

        // assert
        result shouldBeEqualTo true
        coVerify(exactly = 1) { mediaUseCase.getById(id, MediaType.MOVIE) }
    }

    @Test
    fun `getIsLiked should return false when media is not found`() = runTest {
        // arrange
        coEvery { mediaUseCase.getById(any(), any()) } returns null

        // act
        val result = sut.getIsLiked(1L, MediaType.MOVIE)

        // assert
        result shouldBeEqualTo false
    }

    @Test
    fun `toggleLike should return the new liked status from the use case`() = runTest {
        // arrange
        val media = createMediaMock().copy(id = 1L, isLiked = false)
        coEvery { favoriteUseCase(media) } returns UseCaseState.Success(true)

        // act
        val result = sut.toggleLike(media)

        // assert
        result shouldBeEqualTo UseCaseState.Success(true)
        coVerify(exactly = 1) { favoriteUseCase(media) }
    }

    @Test
    fun `toggleLike should return the failure from the use case`() = runTest {
        // arrange
        val media = createMediaMock()
        val failure = UseCaseState.Failure(FailType.Unauthorized)
        coEvery { favoriteUseCase(media) } returns failure

        // act
        val result = sut.toggleLike(media)

        // assert
        result shouldBeEqualTo failure
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
