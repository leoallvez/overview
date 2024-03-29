package br.dev.singular.overview.remote

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import io.github.leoallvez.firebase.RemoteConfigWrapper
import io.github.leoallvez.firebase.RemoteSource
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.junit.Before
import org.junit.Test

class DisplayAdsRemoteConfigTest {

    private lateinit var _remote: DisplayAdsRemoteConfig

    @MockK
    private lateinit var _remoteConfig: FirebaseRemoteConfig

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        _remote = DisplayAdsRemoteConfig(
            _localPermission = true,
            _remoteSource = RemoteConfigWrapper(_remoteConfig)
        )
    }

    @Test
    fun `should be true result when local is true and remote is true`() {
        // Arrange
        val experiment = _remote.copy(_localPermission = true)
        every { _remoteConfig.getBoolean(any()) } returns true
        // Act
        val result = experiment.execute()
        // Assert
        result.shouldBeTrue()
    }

    @Test
    fun `should be false result when local is true and remote is false`() {
        // Arrange
        val experiment = _remote.copy(_localPermission = true)
        every { _remoteConfig.getBoolean(any()) } returns false
        // Act
        val result = experiment.execute()
        // Assert
        result.shouldBeFalse()
    }

    @Test
    fun `should be false result when local is false and remote is false`() {
        // Arrange
        val experiment = _remote.copy(_localPermission = false)
        every { _remoteConfig.getBoolean(any()) } returns false
        // Act
        val result = experiment.execute()
        // Assert
        result.shouldBeFalse()
    }

    @Test
    fun `should be false result when local is false and remote is true`() {
        // Arrange
        val experiment = _remote.copy(_localPermission = false)
        every { _remoteConfig.getBoolean(any()) } returns true
        // Act
        val result = experiment.execute()
        // Assert
        result.shouldBeFalse()
    }

    @Test
    fun `should be call getBoolean() when call on execute()`() {
        // Arrange
        val remoteSource: RemoteSource = mockk()
        val experiment = DisplayAdsRemoteConfig(
            _localPermission = false,
            _remoteSource = remoteSource
        )
        every { remoteSource.getBoolean(any()) } returns true
        // Act
        experiment.execute()
        // Assert
        verify { remoteSource.getBoolean(any()) }
    }
}
