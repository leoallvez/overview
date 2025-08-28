package br.dev.singular.overview.domain.usecase.suggestion

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.MediaParam
import br.dev.singular.overview.domain.repository.Page
import br.dev.singular.overview.domain.model.Suggestion
import br.dev.singular.overview.domain.repository.GetAll
import br.dev.singular.overview.domain.repository.GetPage
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.createMediaMock
import br.dev.singular.overview.domain.usecase.createSuggestionMock
import br.dev.singular.overview.domain.usecase.suggestion.GetAllSuggestionsUseCase.Companion.MAX_MEDIA
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetAllSuggestionsUseCaseTest {
    private lateinit var getterMediaMock: GetPage<Media, MediaParam>
    private lateinit var getterSuggestionMock: GetAll<Suggestion>
    private lateinit var sut: IGetAllSuggestionsUseCase
    private lateinit var suggestionMock: Suggestion
    private lateinit var mediaMock: Media

    @Before
    fun setup() {
        getterMediaMock = mockk()
        getterSuggestionMock = mockk()
        sut = GetAllSuggestionsUseCase(
            getterSuggestionMock,
            getterMediaMock
        )
        suggestionMock = createSuggestionMock()
        mediaMock = createMediaMock()
    }

    @Test
    fun `invoke should return sorted active suggestions with media`() = runBlocking {
        // arrange
        coEvery { getterSuggestionMock.getAll() } returns listOf(
            suggestionMock.copy(order = 2, isActive = true, key = "key1"),
            suggestionMock.copy(order = 1, isActive = true, key = "key2")
        )
        coEvery { getterMediaMock.getPage(any()) } returns Page(items = listOf(mediaMock))

        // act
        val result = sut.invoke()

        // assert
        coVerify { getterSuggestionMock.getAll() }
        coVerify(exactly = 2) { getterMediaMock.getPage(any()) }
        assertTrue(result is UseCaseState.Success)
        assertEquals(2, (result as UseCaseState.Success).data.size)
        assertEquals("key2", result.data.first().key)
    }

    @Test
    fun `invoke should respect MAX_MEDIA constant`() = runBlocking {
        // arrange
        coEvery { getterSuggestionMock.getAll() } returns listOf(suggestionMock)
        coEvery {
            getterMediaMock.getPage(any())
        } returns Page(items = List(MAX_MEDIA * 2) { mediaMock })

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
        coEvery { getterSuggestionMock.getAll() } returns listOf(
            suggestionMock.copy(isActive = true),
            suggestionMock.copy(isActive = false)
        )
        coEvery { getterMediaMock.getPage(any()) } returns Page(items = listOf(mediaMock))

        // act
        val result = sut.invoke()

        // assert
        assertTrue(result is UseCaseState.Success)
        val suggestions = (result as UseCaseState.Success).data
        assertEquals(1, suggestions.size)
        assertTrue(suggestions.all { it.isActive })
    }

    @Test
    fun `invoke should exclude suggestions without medias from the result`() = runBlocking {
        // arrange
        coEvery { getterSuggestionMock.getAll() } returns listOf(suggestionMock, suggestionMock)
        coEvery { getterMediaMock.getPage(any()) } returns Page(items = listOf())

        // act
        val result = sut.invoke()

        // assert
        coVerify { getterSuggestionMock.getAll() }
        coVerify { getterMediaMock.getPage(any()) }
        assertEquals(UseCaseState.Failure(FailType.NothingFound), result)
    }

    @Test
    fun `invoke should return failure when no active suggestions are found`() = runBlocking {
        // arrange
        coEvery { getterSuggestionMock.getAll() } returns emptyList()

        // act
        val result = sut.invoke()

        // assert
        coVerify { getterSuggestionMock.getAll() }
        assertEquals(UseCaseState.Failure(FailType.NothingFound), result)
    }

    @Test
    fun `invoke should return failure when repository throws exception`() = runBlocking {
        // arrange
        coEvery { getterSuggestionMock.getAll() } throws Exception()

        // act
        val result = sut.invoke()

        // assert
        coVerify { getterSuggestionMock.getAll() }
        assertTrue(result is UseCaseState.Failure)
        assertTrue((result as UseCaseState.Failure).type is FailType.Exception)
    }
}
