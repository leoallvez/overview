package io.github.leoallvez.take.experiment

import io.github.leoallvez.firebase.RemoteSource
import io.github.leoallvez.take.data.model.Suggestion
import io.github.leoallvez.take.util.IJsonFileReader
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SuggestionExperimentTest {

    @MockK(relaxed = true)
    private lateinit var remoteSource: RemoteSource

    @MockK
    private lateinit var jsonFileReader: IJsonFileReader

    private lateinit var experiment: AbExperiment<List<Suggestion>>

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        experiment = ListSetupExperiment(jsonFileReader, remoteSource)
    }

    @Test
    fun `when LOCAL returns a JSON the ELEMENTS of LIST are not NULL`() {
        //Arrange
        every { jsonFileReader.read(any()) } returns JSON
        //Act
        val list: List<Suggestion> = experiment.execute()
        val result = list.filter { it == null }
        //Assert
        assertTrue(result.isEmpty())
    }

    @Test
    fun `when LOCAL returns a JSON the return is a NOT EMPTY LIST`() {
        //Arrange
        every { jsonFileReader.read(any()) } returns JSON
        //Act
        val result: List<Suggestion> = experiment.execute()
        //Assert
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun `when LOCAL returns EMPTY String and REMOTE returns a JSON the return is a NOT EMPTY LIST`() {
        //Arrange
        everyLocalAndRemote(local = EMPTY, remote = JSON)
        //Act
        val result: List<Suggestion> = experiment.execute()
        //Assert
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun `when LOCAL returns EMPTY String and REMOTE returns EMPTY String the return is an EMPTY LIST`() {
        //Arrange
        everyLocalAndRemote(local = EMPTY, remote = EMPTY)
        //Act
        val result: List<Suggestion> = experiment.execute()
        //Assert
        assertTrue(result.isEmpty())
    }

    private fun everyLocalAndRemote(local: String, remote: String) {
        every { jsonFileReader.read(any()) } returns local
        every { remoteSource.getString(any()) } returns remote
    }

    companion object {
        const val JSON = """
            [
                {
                    "order": 1, 
                    "title_resource_id": "comedy_title",
                    "api_path": "api/path"
                }
            ]
        """
        const val EMPTY = ""
    }
}