package br.dev.singular.overview.domain.usecase.suggestions

import br.dev.singular.overview.domain.model.Suggestion
import br.dev.singular.overview.domain.repository.Delete
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.createSuggestionMock
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
    private lateinit var deleterMock: Delete<Suggestion>

    @Before
    fun setup() {
        deleterMock = mockk()
        sut = DeleteSuggestionsUseCase(deleterMock)
        suggestionMock = createSuggestionMock()
    }

    @Test
    fun `invoke should return success when a single suggestion is deleted successfully`() = runTest {
        // arrange
        coEvery { deleterMock.delete(suggestionMock) } returns Unit
        // act
        val result = sut.invoke(suggestionMock)
        // assert
        coVerify { deleterMock.delete(suggestionMock) }
        assertEquals(UseCaseState.Success(Unit), result)
    }

    @Test
    fun `invoke should return success when multiple suggestions are deleted successfully`() = runTest {
        // arrange
        val suggestions = arrayOf(suggestionMock, suggestionMock)
        coEvery { deleterMock.delete(*suggestions) } returns Unit
        // act
        val result = sut.invoke(*suggestions)
        // assert
        coVerify { deleterMock.delete(*suggestions) }
        assertEquals(UseCaseState.Success(Unit), result)
    }

    @Test
    fun `invoke should return failure when deleter throws exception`() = runTest {
        // arrange
        coEvery { deleterMock.delete(suggestionMock) } throws Exception()
        // act
        val result = sut.invoke(suggestionMock)
        // assert
        coVerify { deleterMock.delete(suggestionMock) }
        assertTrue(result is UseCaseState.Failure)
        assertTrue((result as UseCaseState.Failure).type is FailType.Exception)
    }
}
