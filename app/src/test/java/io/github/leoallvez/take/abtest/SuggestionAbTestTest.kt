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

class SuggestionAbTestTest {

    @MockK(relaxed = true)
    private lateinit var _remoteSource: RemoteSource

    @MockK(relaxed = true)
    private lateinit var _jsonFileReader: IJsonFileReader

    private lateinit var _experiment: AbTest<List<Suggestion>>

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        _experiment = SuggestionAbTest(_jsonFileReader, _remoteSource)
    }

    @Test
    fun onExecute_parseValidJsonToList_listHasNoNullElement() {
        // Arrange
        every { _jsonFileReader.read(any()) } returns JSON
        // Act
        val list: List<Suggestion?> = _experiment.execute()
        val hasNoNullElement = list.any { it == null }.not()
        // Assert
        assertTrue(hasNoNullElement)
    }

    @Test
    fun onExecute_parseValidJsonToList_listIsNotEmpty() {
        // Arrange
        every { _jsonFileReader.read(any()) } returns JSON
        // Act
        val list: List<Suggestion> = _experiment.execute()
        // Assert
        assertTrue(list.isNotEmpty())
    }

    @Test
    fun onExecute_localReturnsEmptyStringRemoteReturnsJson_listIsNotEmpty() {
        // Arrange
        everyLocalAndRemote(local = EMPTY, remote = JSON)
        // Act
        val list: List<Suggestion> = _experiment.execute()
        // Assert
        assertTrue(list.isNotEmpty())
    }

    @Test
    fun onExecute_localReturnsEmptyStringRemoteReturnsEmptyString_listIsEmpty() {
        // Arrange
        everyLocalAndRemote(local = EMPTY, remote = EMPTY)
        // Act
        val list: List<Suggestion> = _experiment.execute()
        // Assert
        assertTrue(list.isEmpty())
    }

    @Test
    fun onExecute_localReturnsJsonRemoteReturnsEmptyString_listIsNotEmpty() {
        // Arrange
        everyLocalAndRemote(local = JSON, remote = EMPTY)
        // Act
        val list: List<Suggestion> = _experiment.execute()
        // Assert
        assertTrue(list.isNotEmpty())
    }

    private fun everyLocalAndRemote(
        local: String,
        remote: String
    ) {
        every { _jsonFileReader.read(any()) } returns local
        every { _remoteSource.getString(any()) } returns remote
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
