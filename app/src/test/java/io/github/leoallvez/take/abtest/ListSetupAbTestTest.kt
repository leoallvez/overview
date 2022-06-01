package io.github.leoallvez.take.abtest

import io.github.leoallvez.firebase.RemoteSource
import io.github.leoallvez.take.data.model.Suggestion
import io.github.leoallvez.take.util.IJsonFileReader
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ListSetupAbTestTest {

    @MockK(relaxed = true)
    private lateinit var remoteSource: RemoteSource

    @MockK(relaxed = true)
    private lateinit var jsonFileReader: IJsonFileReader

    private lateinit var experiment: AbTest<List<Suggestion>>

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        experiment = SuggestionAbTest(jsonFileReader, remoteSource)
    }

    @Test
    fun onExecute_parseValidJsonToList_listHasNoNullElement() {
        //Arrange
        every { jsonFileReader.read(any()) } returns JSON
        //Act
        val list: List<Suggestion?> = experiment.execute()
        val hasNoNullElement = list.any { it == null }.not()
        //Assert
        assertTrue(hasNoNullElement)
    }

    @Test
    fun onExecute_parseValidJsonToList_listIsNotEmpty() {
        //Arrange
        every { jsonFileReader.read(any()) } returns JSON
        //Act
        val list: List<Suggestion> = experiment.execute()
        //Assert
        assertTrue(list.isNotEmpty())
    }

    @Test
    fun onExecute_localReturnsEmptyStringRemoteReturnsJson_listIsNotEmpty() {
        //Arrange
        everyLocalAndRemote(local = EMPTY, remote = JSON)
        //Act
        val list: List<Suggestion> = experiment.execute()
        //Assert
        assertTrue(list.isNotEmpty())
    }

    @Test
    fun onExecute_localReturnsEmptyStringRemoteReturnsEmptyString_listIsEmpty() {
        //Arrange
        everyLocalAndRemote(local = EMPTY, remote = EMPTY)
        //Act
        val list: List<Suggestion> = experiment.execute()
        //Assert
        assertTrue(list.isEmpty())
    }

    @Test
    fun onExecute_localReturnsJsonRemoteReturnsEmptyString_listIsNotEmpty() {
        //Arrange
        everyLocalAndRemote(local = JSON, remote = EMPTY)
        //Act
        val list: List<Suggestion> = experiment.execute()
        //Assert
        assertTrue(list.isNotEmpty())
    }

    private fun everyLocalAndRemote(
        local: String,
        remote: String
    ) {
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