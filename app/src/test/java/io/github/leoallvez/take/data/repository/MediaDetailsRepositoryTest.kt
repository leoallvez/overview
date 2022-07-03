package io.github.leoallvez.take.data.repository

import io.github.leoallvez.take.data.source.ApiResult.NetworkError
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
    private lateinit var _dataSource: IMediaRemoteDataSource

    private lateinit var _repository: MediaDetailsRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        _repository = MediaDetailsRepository(_dataSource, UnconfinedTestDispatcher())
    }

    @Test
    fun lab1() {
        assertEquals(5, 3 + 2)
    }

    @Test
    fun lab2() = runTest {
        coEvery { _dataSource.getMediaDetailsResult(id = any(), type = any()) } returns NetworkError()
        val result = _repository.getMediaDetails(0, "").single()
        assertEquals(5, 3 + 2)
    }

}