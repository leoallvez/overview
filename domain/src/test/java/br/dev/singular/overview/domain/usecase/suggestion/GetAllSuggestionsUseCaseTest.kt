package br.dev.singular.overview.domain.usecase.suggestion

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.QueryState
import br.dev.singular.overview.domain.model.Suggestion
import br.dev.singular.overview.domain.repository.GetAll
import br.dev.singular.overview.domain.repository.GetPage
import br.dev.singular.overview.domain.repository.Page
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.createMediaMock
import br.dev.singular.overview.domain.usecase.createSuggestionMock
import br.dev.singular.overview.domain.usecase.suggestion.GetAllSuggestionsUseCase.Companion.MAX_MEDIA
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetAllSuggestionsUseCaseTest {
    private lateinit var getterMediaMock: GetPage<Media, QueryState>
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
    fun `invoke should return sorted active suggestions with media`() = runTest {
        // arrange
        val s1 = suggestionMock.copy(order = 2, isActive = true, key = "key1")
        val s2 = suggestionMock.copy(order = 1, isActive = true, key = "key2")
        
        coEvery { getterSuggestionMock.getAll() } returns listOf(s1, s2)
        coEvery { getterMediaMock.getPage(match { it.key == "key1" }) } returns Page(items = listOf(mediaMock))
        coEvery { getterMediaMock.getPage(match { it.key == "key2" }) } returns Page(items = listOf(mediaMock))

        // act
        val result = sut.invoke()

        // assert
        assertTrue(result is UseCaseState.Success)
        val data = (result as UseCaseState.Success).data
        assertEquals(2, data.size)
        // Verify order (by order property)
        assertEquals("key2", data[0].key) // order 1
        assertEquals("key1", data[1].key) // order 2
        
        coVerify(exactly = 1) { getterSuggestionMock.getAll() }
        coVerify(exactly = 1) { getterMediaMock.getPage(match { it.key == "key1" }) }
        coVerify(exactly = 1) { getterMediaMock.getPage(match { it.key == "key2" }) }
    }

    @Test
    fun `invoke should not override media type when suggestion type is ALL`() = runTest {
        // arrange
        val suggestion = suggestionMock.copy(type = MediaType.ALL, isActive = true)
        val mediaWithDifferentType = mediaMock.copy(type = MediaType.TV)
        
        coEvery { getterSuggestionMock.getAll() } returns listOf(suggestion)
        coEvery { getterMediaMock.getPage(any()) } returns Page(items = listOf(mediaWithDifferentType))

        // act
        val result = sut.invoke()

        // assert
        assertTrue(result is UseCaseState.Success)
        val data = (result as UseCaseState.Success).data
        assertEquals(MediaType.TV, data.first().medias.first().type)
    }

    @Test
    fun `invoke should override media type when suggestion type is specific`() = runTest {
        // arrange
        val suggestion = suggestionMock.copy(type = MediaType.MOVIE, isActive = true)
        val mediaWithDifferentType = mediaMock.copy(type = MediaType.TV)
        
        coEvery { getterSuggestionMock.getAll() } returns listOf(suggestion)
        coEvery { getterMediaMock.getPage(any()) } returns Page(items = listOf(mediaWithDifferentType))

        // act
        val result = sut.invoke()

        // assert
        assertTrue(result is UseCaseState.Success)
        val data = (result as UseCaseState.Success).data
        assertEquals(MediaType.MOVIE, data.first().medias.first().type)
    }

    @Test
    fun `invoke should respect MAX_MEDIA constant`() = runTest {
        // arrange
        coEvery { getterSuggestionMock.getAll() } returns listOf(suggestionMock)
        coEvery {
            getterMediaMock.getPage(any())
        } returns Page(items = List(MAX_MEDIA + 5) { mediaMock })

        // act
        val result = sut.invoke()

        // assert
        assertTrue(result is UseCaseState.Success)
        val suggestions = (result as UseCaseState.Success).data
        assertEquals(MAX_MEDIA, suggestions.first().medias.size)
    }

    @Test
    fun `invoke should exclude inactive suggestions from the result`() = runTest {
        // arrange
        coEvery { getterSuggestionMock.getAll() } returns listOf(
            suggestionMock.copy(isActive = true, key = "active"),
            suggestionMock.copy(isActive = false, key = "inactive")
        )
        coEvery { getterMediaMock.getPage(match { it.key == "active" }) } returns Page(items = listOf(mediaMock))

        // act
        val result = sut.invoke()

        // assert
        assertTrue(result is UseCaseState.Success)
        val suggestions = (result as UseCaseState.Success).data
        assertEquals(1, suggestions.size)
        assertEquals("active", suggestions.first().key)
        coVerify(exactly = 0) { getterMediaMock.getPage(match { it.key == "inactive" }) }
    }

    @Test
    fun `invoke should return NothingFound when all active suggestions have no medias`() = runTest {
        // arrange
        coEvery { getterSuggestionMock.getAll() } returns listOf(suggestionMock)
        coEvery { getterMediaMock.getPage(any()) } returns Page(items = listOf())

        // act
        val result = sut.invoke()

        // assert
        assertEquals(UseCaseState.Failure(FailType.NothingFound), result)
    }

    @Test
    fun `invoke should return NothingFound when list of suggestions is empty`() = runTest {
        // arrange
        coEvery { getterSuggestionMock.getAll() } returns emptyList()

        // act
        val result = sut.invoke()

        // assert
        assertEquals(UseCaseState.Failure(FailType.NothingFound), result)
    }

    @Test
    fun `invoke should return Failure with exception when repository throws`() = runTest {
        // arrange
        val expectedException = RuntimeException("Fatal error")
        coEvery { getterSuggestionMock.getAll() } throws expectedException

        // act
        val result = sut.invoke()

        // assert
        assertTrue(result is UseCaseState.Failure)
        assertEquals(expectedException, ((result as UseCaseState.Failure).type as FailType.Exception).throwable)
    }
}
