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

class DisplayAdsExperimentTest  {

    private lateinit var remoteSource: RemoteSource

    @MockK
    private lateinit var remoteConfig: FirebaseRemoteConfig

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        remoteSource = RemoteConfigWrapper(remoteConfig)
    }

    @Test
    fun `when local permission is TRUE and remote permission is TRUE execute returns TRUE`() {
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
    fun `when local permission is TRUE and remote permission is FALSE execute returns FALSE`() {
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
    fun `when local permission is FALSE and remote permission is FALSE execute returns FALSE`() {
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
    fun `when local permission is FALSE and remote permission is TRUE execute returns FALSE`() {
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
    fun `when the execute method is called getBoolean is also called`() {
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
