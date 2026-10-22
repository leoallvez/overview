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
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
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
    private val liked = MutableStateFlow(false)
    private lateinit var sut: MovieDetailsViewModel

    @Before
    fun setup() {
        every { delegate.observeIsLiked(any(), MediaType.MOVIE) } returns liked
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
        liked.value = true
        coEvery { useCase(id) } returns UseCaseState.Success(movie)

        // act
        sut.handleIntent(MovieDetailsIntent.Load(id))

        // assert
        sut.uiState.value shouldBeInstanceOf UiState.Success::class
        val successData = (sut.uiState.value as UiState.Success).data
        successData shouldBeEqualTo movie.toUi(isLiked = true)
        verify(exactly = 1) { delegate.observeIsLiked(id, MediaType.MOVIE) }
        coVerify(exactly = 1) { useCase(id) }
    }

    @Test
    fun `Load intent should update uiState to Error when useCase returns failure`() = runTest {
        // arrange
        val id = 1L
        coEvery { useCase(id) } returns UseCaseState.Failure(FailType.NothingFound)

        // act
        sut.handleIntent(MovieDetailsIntent.Load(id))

        // assert
        sut.uiState.value shouldBeInstanceOf UiState.Error::class
    }

    @Test
    fun `uiState should follow the liked changes after loading`() = runTest {
        // arrange
        val id = 1L
        coEvery { useCase(id) } returns UseCaseState.Success(createMovieDetailsMock().copy(id = id))
        sut.handleIntent(MovieDetailsIntent.Load(id))

        // act
        liked.value = true

        // assert
        (sut.uiState.value as UiState.Success).data?.metadata?.isLiked shouldBeEqualTo true

        // act
        liked.value = false

        // assert
        (sut.uiState.value as UiState.Success).data?.metadata?.isLiked shouldBeEqualTo false
    }

    @Test
    fun `uiState should stay as Error when the liked changes after a failed load`() = runTest {
        // arrange
        val id = 1L
        coEvery { useCase(id) } returns UseCaseState.Failure(FailType.NothingFound)
        sut.handleIntent(MovieDetailsIntent.Load(id))

        // act
        liked.value = true

        // assert
        sut.uiState.value shouldBeInstanceOf UiState.Error::class
    }

    @Test
    fun `Like intent should toggle like and notify favorite added`() = runTest {
        // arrange
        val id = 1L
        coEvery { useCase(id) } returns UseCaseState.Success(createMovieDetailsMock().copy(id = id))
        sut.handleIntent(MovieDetailsIntent.Load(id))

        val uiMedia = (sut.uiState.value as UiState.Success).data!!
        coEvery { delegate.toggleLike(any()) } returns UseCaseState.Success(true)

        // act
        sut.handleIntent(MovieDetailsIntent.Like(uiMedia))

        // assert
        sut.favoriteAdded.value shouldBeEqualTo true
        coVerify(exactly = 1) { delegate.toggleLike(any()) }
    }

    @Test
    fun `Like intent should keep state when toggleLike fails`() = runTest {
        // arrange
        val id = 1L
        val movie = createMovieDetailsMock().copy(id = id)
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
