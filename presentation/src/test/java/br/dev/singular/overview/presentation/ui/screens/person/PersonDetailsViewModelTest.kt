package br.dev.singular.overview.presentation.ui.screens.person

import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.IGetPersonByIdUseCase
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.createPersonMock
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PersonDetailsViewModelTest {

    private val useCase: IGetPersonByIdUseCase = mockk()

    @Test
    fun `onLoad should update uiState to Success when useCase returns data`() = runTest {
        // arrange
        val person = createPersonMock()
        coEvery { useCase(any()) } returns UseCaseState.Success(person)
        val sut = PersonDetailsViewModel(useCase, UnconfinedTestDispatcher(testScheduler))

        // act
        sut.onLoad(1L)

        // assert success state
        val state = sut.uiState.value
        assertTrue("Expected Success state but was $state", state is UiState.Success)
        val successState = state as UiState.Success
        assertEquals(person.name, successState.data?.name)
    }

    @Test
    fun `onLoad should update uiState to Error when useCase returns failure`() = runTest {
        // arrange
        coEvery { useCase(any()) } returns UseCaseState.Failure(FailType.NothingFound)
        val sut = PersonDetailsViewModel(useCase, UnconfinedTestDispatcher(testScheduler))

        // act
        sut.onLoad(1L)

        // assert error state
        val state = sut.uiState.value
        assertTrue("Expected Error state but was $state", state is UiState.Error)
    }
}
