package br.dev.singular.overview.presentation.ui.screens.media.movie

import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.media.IGetMovieDetailsByIdUseCase
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.createMovieDetailsMock
import br.dev.singular.overview.presentation.ui.screens.media.IMediaDetailsDelegate
import br.dev.singular.overview.presentation.ui.screens.media.movie.interaction.MovieDetailsIntent
import br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi.toUi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailsViewModelTest {

    private val delegate: IMediaDetailsDelegate = mockk(relaxed = true)
    private val useCase: IGetMovieDetailsByIdUseCase = mockk()
    private lateinit var sut: MovieDetailsViewModel

    @Before
    fun setup() {
        sut = MovieDetailsViewModel(
            dispatcher = UnconfinedTestDispatcher(),
            delegate = delegate,
            useCase = useCase
        )
    }

    @Test
    fun `Load intent should update uiState to Success when useCase returns data`() = runTest {
        // arrange
        val id = 1L
        val movie = createMovieDetailsMock().copy(id = id)
        coEvery { delegate.getIsLiked(id, MediaType.MOVIE) } returns true
        coEvery { useCase(id) } returns UseCaseState.Success(movie)

        // act
        sut.handleIntent(MovieDetailsIntent.Load(id))

        // assert
        sut.uiState.value shouldBeInstanceOf UiState.Success::class
        val successData = (sut.uiState.value as UiState.Success).data
        successData shouldBeEqualTo movie.toUi(isLiked = true)
        coVerify(exactly = 1) { delegate.getIsLiked(id, MediaType.MOVIE) }
        coVerify(exactly = 1) { useCase(id) }
    }

    @Test
    fun `Load intent should update uiState to Error when useCase returns failure`() = runTest {
        // arrange
        val id = 1L
        coEvery { delegate.getIsLiked(id, MediaType.MOVIE) } returns false
        coEvery { useCase(id) } returns UseCaseState.Failure(FailType.NothingFound)

        // act
        sut.handleIntent(MovieDetailsIntent.Load(id))

        // assert
        sut.uiState.value shouldBeInstanceOf UiState.Error::class
    }

    @Test
    fun `Like intent should toggle like and update uiState metadata`() = runTest {
        // arrange
        val id = 1L
        val movie = createMovieDetailsMock().copy(id = id)
        coEvery { delegate.getIsLiked(id, MediaType.MOVIE) } returns false
        coEvery { useCase(id) } returns UseCaseState.Success(movie)
        sut.handleIntent(MovieDetailsIntent.Load(id))

        val uiMovie = (sut.uiState.value as UiState.Success).data!!
        coEvery { delegate.toggleLike(any()) } returns UseCaseState.Success(true)

        // act
        sut.handleIntent(MovieDetailsIntent.Like(uiMovie))

        // assert
        val updatedState = sut.uiState.value as UiState.Success
        updatedState.data?.metadata?.isLiked shouldBeEqualTo true
        sut.favoriteAdded.value shouldBeEqualTo true
        coVerify(exactly = 1) { delegate.toggleLike(any()) }
    }

    @Test
    fun `Like intent should keep state when toggleLike fails`() = runTest {
        // arrange
        val id = 1L
        val movie = createMovieDetailsMock().copy(id = id)
        coEvery { delegate.getIsLiked(id, MediaType.MOVIE) } returns false
        coEvery { useCase(id) } returns UseCaseState.Success(movie)
        sut.handleIntent(MovieDetailsIntent.Load(id))

        val uiMovie = (sut.uiState.value as UiState.Success).data!!
        coEvery {
            delegate.toggleLike(any())
        } returns UseCaseState.Failure(FailType.Exception(RuntimeException()))

        // act
        sut.handleIntent(MovieDetailsIntent.Like(uiMovie))

        // assert
        val state = sut.uiState.value as UiState.Success
        state.data?.metadata?.isLiked shouldBeEqualTo false
        sut.loginRequired.value shouldBeEqualTo false
        sut.favoriteAdded.value shouldBeEqualTo false
    }

    @Test
    fun `Like intent should require login when the user is not signed in`() = runTest {
        // arrange
        val id = 1L
        val media = createMovieDetailsMock().copy(id = id)
        coEvery { delegate.getIsLiked(id, MediaType.MOVIE) } returns false
        coEvery { useCase(id) } returns UseCaseState.Success(media)
        sut.handleIntent(MovieDetailsIntent.Load(id))

        val uiMedia = (sut.uiState.value as UiState.Success).data!!
        coEvery {
            delegate.toggleLike(any())
        } returns UseCaseState.Failure(FailType.Unauthorized)

        // act
        sut.handleIntent(MovieDetailsIntent.Like(uiMedia))

        // assert
        sut.loginRequired.value shouldBeEqualTo true
        val state = sut.uiState.value as UiState.Success
        state.data?.metadata?.isLiked shouldBeEqualTo false
    }

    @Test
    fun `Like intent should not notify favorite added when the media is unliked`() = runTest {
        // arrange
        coEvery { delegate.toggleLike(any()) } returns UseCaseState.Success(false)

        // act
        sut.handleIntent(MovieDetailsIntent.Like(createMovieDetailsMock().toUi(isLiked = true)))

        // assert
        sut.favoriteAdded.value shouldBeEqualTo false
    }

    @Test
    fun `DismissFavoriteAdded intent should reset favoriteAdded`() = runTest {
        // arrange
        coEvery { delegate.toggleLike(any()) } returns UseCaseState.Success(true)
        sut.handleIntent(MovieDetailsIntent.Like(createMovieDetailsMock().toUi(isLiked = false)))

        // act
        sut.handleIntent(MovieDetailsIntent.DismissFavoriteAdded)

        // assert
        sut.favoriteAdded.value shouldBeEqualTo false
    }

    @Test
    fun `DismissLoginAlert intent should reset loginRequired`() = runTest {
        // arrange
        coEvery {
            delegate.toggleLike(any())
        } returns UseCaseState.Failure(FailType.Unauthorized)
        sut.handleIntent(MovieDetailsIntent.Like(createMovieDetailsMock().toUi(isLiked = false)))

        // act
        sut.handleIntent(MovieDetailsIntent.DismissLoginAlert)

        // assert
        sut.loginRequired.value shouldBeEqualTo false
    }

    @Test
    fun `SelectCatalog intent should call delegate selectCatalog`() = runTest {
        // arrange
        val catalog = br.dev.singular.overview.presentation.model.CatalogUiModel(
            id = 1, name = "Netflix", priority = 1, logoURL = "", previewDrawableRes = null
        )

        // act
        sut.handleIntent(MovieDetailsIntent.SelectCatalog(catalog))

        // assert
        coVerify(exactly = 1) { delegate.selectCatalog(catalog) }
    }
}
