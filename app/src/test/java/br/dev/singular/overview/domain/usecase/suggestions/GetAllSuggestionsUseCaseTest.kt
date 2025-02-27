package br.dev.singular.overview.domain.usecase.suggestions

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.Suggestion
import br.dev.singular.overview.domain.repository.GetAll
import br.dev.singular.overview.domain.repository.IMediaRepository
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.createMediaMock
import br.dev.singular.overview.domain.usecase.createSuggestionMock
import br.dev.singular.overview.domain.usecase.suggetions.GetAllSuggestionsUseCase
import br.dev.singular.overview.domain.usecase.suggetions.GetAllSuggestionsUseCase.Companion.MAX_MEDIA
import br.dev.singular.overview.domain.usecase.suggetions.IGetAllSuggestionsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetAllSuggestionsUseCaseTest {

    private lateinit var mediaRepositoryMock: IMediaRepository
    private lateinit var getterMock: GetAll<Suggestion>
    private lateinit var sut: IGetAllSuggestionsUseCase
    private lateinit var suggestionMock: Suggestion
    private lateinit var mediaMock: Media

    @Before
    fun setup() {
        mediaRepositoryMock = mockk()
        getterMock = mockk()
        sut = GetAllSuggestionsUseCase(mediaRepositoryMock, getterMock)
        suggestionMock = createSuggestionMock()
        mediaMock = createMediaMock()
    }

    @Test
    fun `invoke should return sorted active suggestions with media`() = runBlocking {
        // arrange
        val suggestions = listOf(
            suggestionMock.copy(order = 2, isActive = true, path = "path1"),
            suggestionMock.copy(order = 1, isActive = true, path = "path2")
        )
        coEvery { getterMock.getAll() } returns suggestions
        coEvery { mediaRepositoryMock.getByPath(any()) } returns listOf(mediaMock)

        // act
        val result = sut.invoke()

        // assert
        coVerify { getterMock.getAll() }
        coVerify(exactly = 2) { mediaRepositoryMock.getByPath(any()) }
        assertTrue(result is UseCaseState.Success)
        assertEquals(2, (result as UseCaseState.Success).data.size)
        assertEquals("path2", result.data[0].path)
    }

    @Test
    fun `invoke should respect MAX_MEDIA constant`() = runBlocking {
        // arrange
        coEvery { getterMock.getAll() } returns listOf(suggestionMock)
        coEvery { mediaRepositoryMock.getByPath(any()) } returns List(MAX_MEDIA * 2) { mediaMock }

        // act
        val result = sut.invoke()

        // assert
        assertTrue(result is UseCaseState.Success)
        val suggestions = (result as UseCaseState.Success).data
        assertEquals(MAX_MEDIA, suggestions.first().medias.size)
    }

    @Test
    fun `invoke should exclude inactive suggestions from the result`() = runBlocking {
        // arrange
        val activeSuggestion = suggestionMock.copy(isActive = true)
        val inactiveSuggestion = suggestionMock.copy(isActive = false)
        coEvery { getterMock.getAll() } returns listOf(activeSuggestion, inactiveSuggestion)
        coEvery { mediaRepositoryMock.getByPath(any()) } returns emptyList()

        // act
        val result = sut.invoke()

        // assert
        assertTrue(result is UseCaseState.Success)
        val suggestions = (result as UseCaseState.Success).data
        assertEquals(1, suggestions.size)
        assertTrue(suggestions.all { it.isActive })
    }

    @Test
    fun `invoke should return failure when no active suggestions are found`() = runBlocking {
        // arrange
        coEvery { getterMock.getAll() } returns emptyList()

        // act
        val result = sut.invoke()

        // assert
        coVerify { getterMock.getAll() }
        assertEquals(UseCaseState.Failure(FailType.NothingFound), result)
    }

    @Test
    fun `invoke should return failure when repository throws exception`() = runBlocking {
        // arrange
        coEvery { getterMock.getAll() } throws Exception()

        // act
        val result = sut.invoke()

        // assert
        coVerify { getterMock.getAll() }
        assertTrue(result is UseCaseState.Failure)
        assertTrue((result as UseCaseState.Failure).type is FailType.Exception)
    }
}
