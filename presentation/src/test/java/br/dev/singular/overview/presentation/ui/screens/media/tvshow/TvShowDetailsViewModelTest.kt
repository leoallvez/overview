package br.dev.singular.overview.presentation.ui.screens.media.tvshow

import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.media.IGetTvShowDetailsByIdUseCase
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.createTvShowDetailsMock
import br.dev.singular.overview.presentation.ui.screens.media.IMediaDetailsDelegate
import br.dev.singular.overview.presentation.ui.screens.media.tvshow.interaction.TvShowDetailsIntent
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
class TvShowDetailsViewModelTest {

    private val delegate: IMediaDetailsDelegate = mockk(relaxed = true)
    private val useCase: IGetTvShowDetailsByIdUseCase = mockk()
    private lateinit var sut: TvShowDetailsViewModel

    @Before
    fun setup() {
        sut = TvShowDetailsViewModel(
            dispatcher = UnconfinedTestDispatcher(),
            delegate = delegate,
            useCase = useCase
        )
    }

    @Test
    fun `Load intent should update uiState to Success when useCase returns data`() = runTest {
        // arrange
        val id = 1L
        val tvShow = createTvShowDetailsMock().copy(id = id)
        coEvery { delegate.getIsLiked(id) } returns true
        coEvery { useCase(id) } returns UseCaseState.Success(tvShow)

        // act
        sut.handleIntent(TvShowDetailsIntent.Load(id))

        // assert
        sut.uiState.value shouldBeInstanceOf UiState.Success::class
        val successData = (sut.uiState.value as UiState.Success).data
        successData shouldBeEqualTo tvShow.toUi(isLiked = true)
        coVerify(exactly = 1) { delegate.getIsLiked(id) }
        coVerify(exactly = 1) { useCase(id) }
    }

    @Test
    fun `Load intent should update uiState to Error when useCase returns failure`() = runTest {
        // arrange
        val id = 1L
        coEvery { delegate.getIsLiked(id) } returns false
        coEvery { useCase(id) } returns UseCaseState.Failure(FailType.NothingFound)

        // act
        sut.handleIntent(TvShowDetailsIntent.Load(id))

        // assert
        sut.uiState.value shouldBeInstanceOf UiState.Error::class
    }

    @Test
    fun `Like intent should toggle like and update uiState metadata`() = runTest {
        // arrange
        val id = 1L
        val tvShow = createTvShowDetailsMock().copy(id = id)
        coEvery { delegate.getIsLiked(id) } returns false
        coEvery { useCase(id) } returns UseCaseState.Success(tvShow)
        sut.handleIntent(TvShowDetailsIntent.Load(id))

        val uiTvShow = (sut.uiState.value as UiState.Success).data!!
        coEvery { delegate.toggleLike(any()) } returns true

        // act
        sut.handleIntent(TvShowDetailsIntent.Like(uiTvShow))

        // assert
        val updatedState = sut.uiState.value as UiState.Success
        updatedState.data?.metadata?.isLiked shouldBeEqualTo true
        coVerify(exactly = 1) { delegate.toggleLike(any()) }
    }

    @Test
    fun `Like intent should rollback state when toggleLike throws exception`() = runTest {
        // arrange
        val id = 1L
        val tvShow = createTvShowDetailsMock().copy(id = id)
        coEvery { delegate.getIsLiked(id) } returns false
        coEvery { useCase(id) } returns UseCaseState.Success(tvShow)
        sut.handleIntent(TvShowDetailsIntent.Load(id))

        val uiTvShow = (sut.uiState.value as UiState.Success).data!!
        coEvery { delegate.toggleLike(any()) } throws RuntimeException()

        // act
        sut.handleIntent(TvShowDetailsIntent.Like(uiTvShow))

        // assert
        val state = sut.uiState.value as UiState.Success
        state.data?.metadata?.isLiked shouldBeEqualTo false
    }

    @Test
    fun `SelectCatalog intent should call delegate selectCatalog`() = runTest {
        // arrange
        val catalog = br.dev.singular.overview.presentation.model.CatalogUiModel(
            id = 1, name = "Netflix", priority = 1, logoURL = "", previewDrawableRes = null
        )

        // act
        sut.handleIntent(TvShowDetailsIntent.SelectCatalog(catalog))

        // assert
        coVerify(exactly = 1) { delegate.selectCatalog(catalog) }
    }
}
