package br.dev.singular.overview.presentation.ui.screens.media

import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.QueryState
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.ICatalogQueryStateUseCase
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.media.IObserveFavoritesUseCase
import br.dev.singular.overview.domain.usecase.media.IToggleFavoriteUseCase
import br.dev.singular.overview.presentation.createCatalogUiModelMock
import br.dev.singular.overview.presentation.createMediaMock
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test

class MediaDetailsDelegateTest {

    @MockK
    private lateinit var favoritesUseCase: IObserveFavoritesUseCase

    @MockK
    private lateinit var queryUseCase: ICatalogQueryStateUseCase

    @MockK
    private lateinit var favoriteUseCase: IToggleFavoriteUseCase

    private lateinit var sut: IMediaDetailsDelegate

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        sut = MediaDetailsDelegate(favoritesUseCase, queryUseCase, favoriteUseCase)
    }

    @Test
    fun `observeIsLiked should emit true when media is among the favorites`() = runTest {
        // arrange
        val media = createMediaMock().copy(id = 1L, type = MediaType.MOVIE)
        every { favoritesUseCase() } returns flowOf(listOf(media))

        // act
        val result = sut.observeIsLiked(1L, MediaType.MOVIE).toList()

        // assert
        result shouldBeEqualTo listOf(true)
    }

    @Test
    fun `observeIsLiked should emit false when only the other media type is a favorite`() = runTest {
        // arrange
        val media = createMediaMock().copy(id = 1L, type = MediaType.TV)
        every { favoritesUseCase() } returns flowOf(listOf(media))

        // act
        val result = sut.observeIsLiked(1L, MediaType.MOVIE).toList()

        // assert
        result shouldBeEqualTo listOf(false)
    }

    @Test
    fun `observeIsLiked should emit only when the liked status changes`() = runTest {
        // arrange
        val media = createMediaMock().copy(id = 1L, type = MediaType.MOVIE)
        val other = createMediaMock().copy(id = 2L, type = MediaType.MOVIE)
        every { favoritesUseCase() } returns
            flowOf(emptyList(), listOf(other), listOf(other, media), listOf(media), emptyList())

        // act
        val result = sut.observeIsLiked(1L, MediaType.MOVIE).toList()

        // assert
        result shouldBeEqualTo listOf(false, true, false)
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
