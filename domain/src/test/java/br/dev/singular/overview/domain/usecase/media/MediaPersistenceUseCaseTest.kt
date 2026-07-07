package br.dev.singular.overview.domain.usecase.media

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.repository.GetById
import br.dev.singular.overview.domain.repository.Update
import br.dev.singular.overview.domain.usecase.createMediaMock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class MediaPersistenceUseCaseTest {

    private lateinit var sut: IMediaPersistenceUseCase

    private lateinit var getter: GetById<Media>
    private lateinit var updater: Update<Media>

    @Before
    fun setup() {
        getter = mockk()
        updater = mockk()
        sut = MediaPersistenceUseCase(getter, updater)
    }

    @Test
    fun `getById should return media from getter`() = runTest {
        // arrange
        val media = createMediaMock()
        coEvery { getter.getById(1L) } returns media

        // act
        val result = sut.getById(1L)

        // assert
        coVerify(exactly = 1) { getter.getById(1L) }
        assertEquals(media, result)
    }

    @Test
    fun `getById should return null when getter returns null`() = runTest {
        // arrange
        coEvery { getter.getById(1L) } returns null

        // act
        val result = sut.getById(1L)

        // assert
        coVerify(exactly = 1) { getter.getById(1L) }
        assertNull(result)
    }

    @Test
    fun `save should call updater with media`() = runTest {
        // arrange
        val media = createMediaMock()
        coEvery { updater.update(media) } returns Unit

        // act
        sut.save(media)

        // assert
        coVerify(exactly = 1) { updater.update(media) }
    }
}
