package io.github.leoallvez.take.experiment

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

class DisplayAdsExperimentTest {

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
        val experiment = DisplayAdsExperiment(
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
        val experiment = DisplayAdsExperiment(
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
        val experiment = DisplayAdsExperiment(
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
        val experiment = DisplayAdsExperiment(
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
        val experiment = DisplayAdsExperiment(
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
