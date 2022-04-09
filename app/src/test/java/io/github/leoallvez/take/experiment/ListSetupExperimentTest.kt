package io.github.leoallvez.take.experiment

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import io.github.leoallvez.firebase.RemoteConfigWrapper
import io.github.leoallvez.firebase.RemoteSource
import io.github.leoallvez.take.data.model.ListSetup
import io.github.leoallvez.take.util.IJsonFileReader
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ListSetupExperimentTest {

    @MockK
    private lateinit var jsonFileReader: IJsonFileReader

    private lateinit var remoteSource: RemoteSource

    @MockK
    private lateinit var remoteConfig: FirebaseRemoteConfig

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        remoteSource = RemoteConfigWrapper(remoteConfig)
    }

    @Test
    fun lab() {
        //Arrange
        val experiment = ListSetupExperiment(jsonFileReader, remoteSource)
        every { remoteConfig.getString(any()) } returns """[{"list_name": "Comedian"}]"""
        //Act
        val result: ListSetup = experiment.execute().first()
        //Assert
        assertEquals("Comedian", result.listName)
    }
}