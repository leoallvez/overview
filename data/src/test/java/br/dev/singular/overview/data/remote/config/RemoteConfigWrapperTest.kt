package br.dev.singular.overview.data.remote.config

import com.google.android.gms.tasks.Tasks
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

class RemoteConfigWrapperTest {

    private val firebaseRemoteConfig: FirebaseRemoteConfig = mockk(relaxed = true)
    private val sut = RemoteConfigWrapper(firebaseRemoteConfig)

    @Test
    fun `getString should return value from firebase`() {
        // arrange
        val key = RemoteConfigKey.SUGGESTIONS_KEY
        every { firebaseRemoteConfig.getString(key.value) } returns "value"

        // act
        val result = sut.getString(key)

        // assert
        result shouldBeEqualTo "value"
    }

    @Test
    fun `getBoolean should return value from firebase`() {
        // arrange
        val key = RemoteConfigKey.DISPLAY_ADS_KEY
        every { firebaseRemoteConfig.getBoolean(key.value) } returns true

        // act
        val result = sut.getBoolean(key)

        // assert
        result shouldBeEqualTo true
    }

    @Test
    fun `waitAndActivate should return success from firebase`() = runTest {
        // arrange
        every { firebaseRemoteConfig.fetchAndActivate() } returns Tasks.forResult(true)

        // act
        val result = sut.waitAndActivate()

        // assert
        result shouldBeEqualTo true
    }

    @Test
    fun `start should configure settings`() {
        // act
        sut.start()

        // assert
        verify { firebaseRemoteConfig.setConfigSettingsAsync(any()) }
        verify { firebaseRemoteConfig.fetch() }
    }
}
