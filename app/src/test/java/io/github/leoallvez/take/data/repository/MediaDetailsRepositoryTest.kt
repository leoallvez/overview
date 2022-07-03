package io.github.leoallvez.take.data.repository

import io.github.leoallvez.take.data.source.NetworkResult.NetworkError
import io.github.leoallvez.take.data.source.mediaitem.IMediaRemoteDataSource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MediaDetailsRepositoryTest {

    @MockK
    private lateinit var dataSource: IMediaRemoteDataSource

    private lateinit var repository: MediaDetailsRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repository = MediaDetailsRepository(dataSource, UnconfinedTestDispatcher())
    }

    @Test
    fun lab1() {
        assertEquals(5, 3 + 2)
    }

    @Test
    fun lab2() = runTest {
        coEvery { dataSource.getMediaDetailsResult(id = any(), type = any()) } returns NetworkError()
        val result = repository.getMediaDetails(0, "").single()
        assertEquals(5, 3 + 2)
    }

}