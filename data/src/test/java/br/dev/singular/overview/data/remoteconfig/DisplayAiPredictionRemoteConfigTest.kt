package br.dev.singular.overview.data.remoteconfig

import br.dev.singular.overview.data.remoteconfig.RemoteConfigKey.DISPLAY_AI_PREDICTION_KEY
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DisplayAiPredictionRemoteConfigTest {

    private val remoteSource: IRemoteConfigProvider = mockk()

    @Test
    fun `execute should return true when localOverride is true even if remote is false`() {
        // arrange
        val sut =
            DisplayAiPredictionRemoteConfig(_localOverride = true, _remoteSource = remoteSource)
        every { remoteSource.getBoolean(DISPLAY_AI_PREDICTION_KEY) } returns false

        // act
        val result = sut.execute()

        // assert
        assertTrue(result)
        verify(exactly = 0) { remoteSource.getBoolean(any()) }
    }

    @Test
    fun `execute should return false when localOverride is false even if remote is true`() {
        // arrange
        val sut =
            DisplayAiPredictionRemoteConfig(_localOverride = false, _remoteSource = remoteSource)
        every { remoteSource.getBoolean(DISPLAY_AI_PREDICTION_KEY) } returns true

        // act
        val result = sut.execute()

        // assert
        assertFalse(result)
        verify(exactly = 0) { remoteSource.getBoolean(any()) }
    }

    @Test
    fun `execute should return the remote value true when localOverride is null`() {
        // arrange
        val sut =
            DisplayAiPredictionRemoteConfig(_localOverride = null, _remoteSource = remoteSource)
        every { remoteSource.getBoolean(DISPLAY_AI_PREDICTION_KEY) } returns true

        // act
        val result = sut.execute()

        // assert
        assertTrue(result)
    }

    @Test
    fun `execute should return the remote value false when localOverride is null`() {
        // arrange
        val sut =
            DisplayAiPredictionRemoteConfig(_localOverride = null, _remoteSource = remoteSource)
        every { remoteSource.getBoolean(DISPLAY_AI_PREDICTION_KEY) } returns false

        // act
        val result = sut.execute()

        // assert
        assertFalse(result)
    }
}
