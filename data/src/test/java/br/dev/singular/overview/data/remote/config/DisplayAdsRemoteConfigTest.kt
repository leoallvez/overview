package br.dev.singular.overview.data.remote.config

import br.dev.singular.overview.data.remote.config.RemoteConfigKey.DISPLAY_ADS_KEY
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DisplayAdsRemoteConfigTest {

    private val remoteSource: IRemoteConfigProvider = mockk()

    @Test
    fun `execute should return true when localPermission is true`() {
        // arrange
        val sut = DisplayAdsRemoteConfig(_localPermission = true, _remoteSource = remoteSource)
        every { remoteSource.getBoolean(DISPLAY_ADS_KEY) } returns false

        // act
        val result = sut.execute()

        // assert
        assertTrue(result)
    }

    @Test
    fun `execute should return true when remotePermission is true`() {
        // arrange
        val sut = DisplayAdsRemoteConfig(_localPermission = false, _remoteSource = remoteSource)
        every { remoteSource.getBoolean(DISPLAY_ADS_KEY) } returns true

        // act
        val result = sut.execute()

        // assert
        assertTrue(result)
    }

    @Test
    fun `execute should return false when both local and remote permissions are false`() {
        // arrange
        val sut = DisplayAdsRemoteConfig(_localPermission = false, _remoteSource = remoteSource)
        every { remoteSource.getBoolean(DISPLAY_ADS_KEY) } returns false

        // act
        val result = sut.execute()

        // assert
        assertFalse(result)
    }
}
