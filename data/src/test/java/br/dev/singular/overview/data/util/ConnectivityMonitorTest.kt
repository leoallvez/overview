package br.dev.singular.overview.data.util

import android.net.ConnectivityManager
import android.net.Network
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ConnectivityMonitorTest {

    private val connectivityManager: ConnectivityManager = mockk()
    private val network: Network = mockk()
    private val callback = slot<ConnectivityManager.NetworkCallback>()
    private val sut = ConnectivityMonitor(connectivityManager)

    @Before
    fun setup() {
        justRun { connectivityManager.registerDefaultNetworkCallback(capture(callback)) }
        justRun { connectivityManager.unregisterNetworkCallback(any<ConnectivityManager.NetworkCallback>()) }
    }

    @Test
    fun `isOnline should emit true when there is an active network`() = runTest {
        // arrange
        every { connectivityManager.activeNetwork } returns network
        val values = mutableListOf<Boolean>()

        // act
        val job = launch(UnconfinedTestDispatcher(testScheduler)) { sut.isOnline.toList(values) }

        // assert
        values shouldBeEqualTo listOf(true)
        job.cancel()
    }

    @Test
    fun `isOnline should emit false when there is no active network`() = runTest {
        // arrange
        every { connectivityManager.activeNetwork } returns null
        val values = mutableListOf<Boolean>()

        // act
        val job = launch(UnconfinedTestDispatcher(testScheduler)) { sut.isOnline.toList(values) }

        // assert
        values shouldBeEqualTo listOf(false)
        job.cancel()
    }

    @Test
    fun `isOnline should follow the network being lost and becoming available again`() = runTest {
        // arrange
        every { connectivityManager.activeNetwork } returns network
        val values = mutableListOf<Boolean>()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) { sut.isOnline.toList(values) }

        // act
        callback.captured.onLost(network)
        callback.captured.onAvailable(network)

        // assert
        values shouldBeEqualTo listOf(true, false, true)
        job.cancel()
    }

    @Test
    fun `isOnline should not repeat the same state`() = runTest {
        // arrange
        every { connectivityManager.activeNetwork } returns network
        val values = mutableListOf<Boolean>()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) { sut.isOnline.toList(values) }

        // act
        callback.captured.onAvailable(network)
        callback.captured.onAvailable(network)

        // assert
        values shouldBeEqualTo listOf(true)
        job.cancel()
    }

    @Test
    fun `isOnline should unregister the callback when the collection is cancelled`() = runTest {
        // arrange
        every { connectivityManager.activeNetwork } returns network
        val job = launch(UnconfinedTestDispatcher(testScheduler)) { sut.isOnline.toList() }

        // act
        job.cancel()
        testScheduler.advanceUntilIdle()

        // assert
        verify(exactly = 1) { connectivityManager.unregisterNetworkCallback(callback.captured) }
    }
}
