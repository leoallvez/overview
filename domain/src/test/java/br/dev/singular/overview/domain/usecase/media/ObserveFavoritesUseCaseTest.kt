package br.dev.singular.overview.domain.usecase.media

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.repository.Observe
import br.dev.singular.overview.domain.usecase.createMediaMock
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ObserveFavoritesUseCaseTest {

    private lateinit var sut: IObserveFavoritesUseCase

    private val observer: Observe<List<Media>> = mockk()

    @Before
    fun setup() {
        sut = ObserveFavoritesUseCase(observer)
    }

    @Test
    fun `invoke should return every favorites list emitted by the repository`() = runTest {
        // arrange
        val favorites = listOf(createMediaMock())
        every { observer.observe() } returns flowOf(emptyList(), favorites)

        // act
        val result = sut().toList()

        // assert
        assertEquals(listOf(emptyList(), favorites), result)
        verify(exactly = 1) { observer.observe() }
    }
}
