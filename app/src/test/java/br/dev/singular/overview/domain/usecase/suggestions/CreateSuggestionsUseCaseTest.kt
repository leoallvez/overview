package br.dev.singular.overview.domain.usecase.suggestions

import br.dev.singular.overview.domain.model.Suggestion
import br.dev.singular.overview.domain.repository.Create
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.createDomainSuggestionMock
import br.dev.singular.overview.domain.usecase.suggetions.CreateSuggestionsUseCase
import br.dev.singular.overview.domain.usecase.suggetions.ICreateSuggestionsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


class CreateSuggestionsUseCaseTest {

    private lateinit var suggestionMock: Suggestion
    private lateinit var sut: ICreateSuggestionsUseCase
    private lateinit var creatorMock: Create<Suggestion>

    @Before
    fun setup() {
        creatorMock = mockk()
        sut = CreateSuggestionsUseCase(creatorMock)
        suggestionMock = createDomainSuggestionMock()
    }

    @Test
    fun `invoke should return success when suggestions are valid`() = runBlocking {
        // arrange
        coEvery { creatorMock.create(suggestionMock) } returns Unit
        // act
        val result = sut.invoke(suggestionMock)
        // assert
        coVerify { creatorMock.create(suggestionMock) }
        assertEquals(UseCaseState.Success(Unit), result)
    }

    @Test
    fun `invoke should return success when multiple suggestions are valid`() = runBlocking {
        // arrange
        val suggestions = arrayOf(suggestionMock, suggestionMock)
        coEvery { creatorMock.create(*suggestions) } returns Unit
        // act
        val result = sut.invoke(*suggestions)
        // assert
        coVerify { creatorMock.create(*suggestions) }
        assertEquals(UseCaseState.Success(Unit), result)
    }

    @Test
    fun `invoke should return failure when creator throws exception`() = runBlocking {
        // arrange
        coEvery { creatorMock.create(suggestionMock) } throws Exception()
        // act
        val result = sut.invoke(suggestionMock)
        // assert
        coVerify { creatorMock.create(suggestionMock) }
        assertTrue(result is UseCaseState.Failure)
        assertTrue((result as UseCaseState.Failure).type is FailType.Exception)
    }
}
