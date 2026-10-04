package br.dev.singular.overview.domain.usecase.media

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.MediaKey
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.repository.GetByParam
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

    private lateinit var getter: GetByParam<Media?, MediaKey>
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
        coEvery { getter.getByParam(MediaKey(1L, MediaType.MOVIE)) } returns media

        // act
        val result = sut.getById(1L, MediaType.MOVIE)

        // assert
        coVerify(exactly = 1) { getter.getByParam(MediaKey(1L, MediaType.MOVIE)) }
        assertEquals(media, result)
    }

    @Test
    fun `getById should return null when getter returns null`() = runTest {
        // arrange
        coEvery { getter.getByParam(MediaKey(1L, MediaType.MOVIE)) } returns null

        // act
        val result = sut.getById(1L, MediaType.MOVIE)

        // assert
        coVerify(exactly = 1) { getter.getByParam(MediaKey(1L, MediaType.MOVIE)) }
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
