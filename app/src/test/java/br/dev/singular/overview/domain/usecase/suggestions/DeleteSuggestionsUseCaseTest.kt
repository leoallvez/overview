package br.dev.singular.overview.domain.usecase.suggestions

import br.dev.singular.overview.domain.model.Suggestion
import br.dev.singular.overview.domain.repository.Delete
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.createDomainSuggestionMock
import br.dev.singular.overview.domain.usecase.suggetions.DeleteSuggestionsUseCase
import br.dev.singular.overview.domain.usecase.suggetions.IDeleteSuggestionsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeleteSuggestionsUseCaseTest {

    private lateinit var suggestionMock: Suggestion
    private lateinit var sut: IDeleteSuggestionsUseCase
    private lateinit var suggestionDeleterMock: Delete<Suggestion>

    @Before
    fun setup() {
        suggestionDeleterMock = mockk()
        sut = DeleteSuggestionsUseCase(suggestionDeleterMock)
        suggestionMock = createDomainSuggestionMock()
    }

    @Test
    fun `invoke should return success when a single suggestion is deleted successfully`() = runTest {
        // arrange
        coEvery { suggestionDeleterMock.delete(suggestionMock) } returns Unit
        // act
        val result = sut.invoke(suggestionMock)
        // assert
        coVerify { suggestionDeleterMock.delete(suggestionMock) }
        assertEquals(UseCaseState.Success(Unit), result)
    }

    @Test
    fun `invoke should return success when multiple suggestions are deleted successfully`() = runTest {
        // arrange
        val suggestions = arrayOf(suggestionMock, suggestionMock)
        coEvery { suggestionDeleterMock.delete(*suggestions) } returns Unit
        // act
        val result = sut.invoke(*suggestions)
        // assert
        coVerify { suggestionDeleterMock.delete(*suggestions) }
        assertEquals(UseCaseState.Success(Unit), result)
    }

    @Test
    fun `invoke should return failure when deleter throws exception`() = runTest {
        // arrange
        coEvery { suggestionDeleterMock.delete(suggestionMock) } throws Exception()
        // act
        val result = sut.invoke(suggestionMock)
        // assert
        coVerify { suggestionDeleterMock.delete(suggestionMock) }
        assertTrue(result is UseCaseState.Failure)
        assertTrue((result as UseCaseState.Failure).type is FailType.Exception)
    }
}
