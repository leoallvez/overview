package io.github.leoallvez.take.abtest

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import io.github.leoallvez.firebase.RemoteConfigWrapper
import io.github.leoallvez.firebase.RemoteSource
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DisplayAdsAbTestTest {

    private lateinit var remoteSource: RemoteSource

    @MockK
    private lateinit var remoteConfig: FirebaseRemoteConfig

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        remoteSource = RemoteConfigWrapper(remoteConfig)
    }

    @Test
    fun onExecute_localIsTrueRemoteIsTrue_isTrue() {
        //Arrange
        val experiment = DisplayAdsAbTest(
            localPermission = true,
            remoteSource
        )
        every { remoteConfig.getBoolean(any()) } returns true
        //Act
        val result = experiment.execute()
        //Assert
        assertEquals(true, result)
    }

    @Test
    fun onExecute_localIsTrueRemoteIsFalse_isTrue() {
        //Arrange
        val experiment = DisplayAdsAbTest(
            localPermission = true,
            remoteSource
        )
        every { remoteConfig.getBoolean(any()) } returns false
        //Act
        val result = experiment.execute()
        //Assert
        assertEquals(false, result)
    }

    @Test
    fun onExecute_localIsFalseRemoteIsFalse_isFalse() {
        //Arrange
        val experiment = DisplayAdsAbTest(
            localPermission = false,
            remoteSource
        )
        every { remoteConfig.getBoolean(any()) } returns false
        //Act
        val result = experiment.execute()
        //Assert
        assertEquals(false, result)
    }

    @Test
    fun onExecute_localIsFalseRemoteIsTrue_isFalse() {
        //Arrange
        val experiment = DisplayAdsAbTest(
            localPermission = false,
            remoteSource
        )
        every { remoteConfig.getBoolean(any()) } returns true
        //Act
        val result = experiment.execute()
        //Assert
        assertEquals(false, result)
    }

    @Test
    fun onExecute_getBooleanIsCalled() {
        //Arrange
        remoteSource = mockk()
        val experiment = DisplayAdsAbTest(
            localPermission = false,
            remoteSource
        )
        every { remoteSource.getBoolean(any()) } returns true
        //Act
        experiment.execute()
        //Assert
        verify { remoteSource.getBoolean(any()) }
    }
}
