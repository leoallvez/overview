package br.com.deepbyte.overview.abtesting

import br.com.deepbyte.overview.data.model.Suggestion
import io.mockk.MockKAnnotations
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SuggestionAbTestingTest : LocalAndRemoteTest() {

    private lateinit var _experiment: AbTesting<List<Suggestion>>

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        _experiment = SuggestionAbTesting(jsonFileReader, remoteSource)
    }

    @Test
    fun execute_localIsNotEmptyAndRemoteIsEmpty_listIsNotEmpty() {
        // Arrange
        everyLocalAndRemote(local = JSON, remote = EMPTY)
        // Act
        val list = _experiment.execute()
        // Assert
        Assert.assertTrue(list.isNotEmpty())
    }

    @Test
    fun execute_localIsEmptyAndRemoteNotIsEmpty_listIsNotEmpty() {
        // Arrange
        everyLocalAndRemote(local = EMPTY, remote = JSON)
        // Act
        val list = _experiment.execute()
        // Assert
        Assert.assertTrue(list.isNotEmpty())
    }

    @Test
    fun execute_localIsEmptyAndRemoteIsEmpty_listIsEmpty() {
        // Arrange
        everyLocalAndRemote(local = EMPTY, remote = EMPTY)
        // Act
        val list = _experiment.execute()
        // Assert
        Assert.assertTrue(list.isEmpty())
    }

    @Test
    fun execute_localIsInvalidAndRemoteIsValid_listIsEmpty() {
        // Arrange
        everyLocalAndRemote(local = INVALID_JSON, remote = INVALID_JSON)
        // Act
        val list = _experiment.execute()
        // Assert
        Assert.assertTrue(list.isEmpty())
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
